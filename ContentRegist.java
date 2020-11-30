package mycalendar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ContentRegist extends Page {
	JPanel p_content;
	JTextField t_regdate;
	JTextField t_title, t_id;
	JTextArea area_content;
	JScrollPane content;
	JButton bt_regist, bt_back;
	JLabel la_title, la_date;
	JLabel la_titlename, la_content, la_regdate, la_id;

	public ContentRegist(MyCalendarMain myCalendarMain) {
		super(myCalendarMain);
		p_content = new JPanel();
		t_id = new JTextField();
		t_regdate = new JTextField();
		t_title = new JTextField();
		area_content = new JTextArea();
		content = new JScrollPane(area_content);

		la_title = new JLabel("일정등록");
		la_id = new JLabel("작성자");
		la_regdate = new JLabel("날짜");
		la_titlename = new JLabel("제목");
		la_content = new JLabel("내용");
		bt_regist = new JButton("등록");
		bt_back = new JButton("뒤로가기");

		p_content.setPreferredSize(new Dimension(400, 430));
		p_content.setBackground(Color.WHITE);
		Dimension d = new Dimension(340, 25);
		t_id.setPreferredSize(d);
		t_regdate.setPreferredSize(d);
		t_title.setPreferredSize(d);
		area_content.setLineWrap(true);
		content.setPreferredSize(new Dimension(340, 140));
		Dimension d2 = new Dimension(30, 25);
		la_id.setPreferredSize(d);
		la_regdate.setPreferredSize(d);
		la_titlename.setPreferredSize(d);
		la_content.setPreferredSize(d);
		la_title.setPreferredSize(new Dimension(370, 25));
		la_title.setHorizontalAlignment(JLabel.CENTER);
		la_title.setFont(new Font(null, Font.BOLD, 25));

		p_content.add(la_title);
		p_content.add(la_id);
		p_content.add(t_id);
		p_content.add(la_regdate);
		p_content.add(t_regdate);
		p_content.add(la_titlename);
		p_content.add(t_title);
		p_content.add(la_content);
		p_content.add(content);
		p_content.add(bt_back);
		p_content.add(bt_regist);

		add(p_content);

		bt_back.addActionListener((e) -> {
			getMyCalendarMain().showPage(MyCalendarMain.HOME);
		});

		bt_regist.addActionListener((e) -> {
			ContentVO vo = new ContentVO();
			vo.setRegdate(t_regdate.getText());
			vo.setTitle(t_title.getText());
			vo.setContent(area_content.getText());

			int result = regist(vo);

			if (result == 0) {
				JOptionPane.showMessageDialog(ContentRegist.this, "등록실패");
			} else {
				JOptionPane.showMessageDialog(ContentRegist.this, "일정등록 성공");
				getMyCalendarMain().showPage(MyCalendarMain.HOME);
				getMyCalendarMain().tableUpdate();
			}
		});
	}

	public int regist(ContentVO contentvo) {
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "insert into content(content_id, member_id, title, content, regdate)";
		sql += " values(seq_content.nextval,?,?,?,?)";

		try {
			pstmt = getMyCalendarMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, getMyCalendarMain().getMember().getMember_id());
			pstmt.setString(2, contentvo.getTitle());
			pstmt.setString(3, contentvo.getContent());
			pstmt.setString(4, contentvo.getRegdate());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getMyCalendarMain().getDbManager().close(pstmt);
		}
		return result;
	}
	
	public void init() {
		t_regdate.setText("");
		t_title.setText("");
		area_content.setText("");
	}

}
