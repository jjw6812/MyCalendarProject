package mycalendar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ContentDetail extends Page {

	JPanel p_content;
	JTextField t_regdate;
	JTextField t_title;
	JTextArea area_content;
	JScrollPane content;
	JButton bt_edit, bt_remove, bt_back;
	JLabel la_title;
	JLabel la_titlename, la_content, la_regdate;
	ContentVO contentvo;

	public ContentDetail(MyCalendarMain myCalendarMain) {
		super(myCalendarMain);
		p_content = new JPanel();

		t_regdate = new JTextField();
		t_title = new JTextField();
		area_content = new JTextArea();
		content = new JScrollPane(area_content);

		la_title = new JLabel("일정보기");
		la_regdate = new JLabel("날짜");
		la_titlename = new JLabel("제목");
		la_content = new JLabel("내용");
		bt_edit = new JButton("수정");
		bt_remove = new JButton("삭제");
		bt_back = new JButton("뒤로가기");

		p_content.setPreferredSize(new Dimension(400, 370));
		p_content.setBackground(Color.WHITE);
		Dimension d = new Dimension(340, 25);
		t_regdate.setPreferredSize(d);
		t_title.setPreferredSize(d);
		area_content.setLineWrap(true);
		content.setPreferredSize(new Dimension(340, 140));
		Dimension d2 = new Dimension(30, 25);
		la_regdate.setPreferredSize(d);
		la_titlename.setPreferredSize(d);
		la_content.setPreferredSize(d);
		la_title.setPreferredSize(new Dimension(370, 25));
		la_title.setHorizontalAlignment(JLabel.CENTER);
		la_title.setFont(new Font(null, Font.BOLD, 25));

		p_content.add(la_title);
		p_content.add(la_regdate);
		p_content.add(t_regdate);
		p_content.add(la_titlename);
		p_content.add(t_title);
		p_content.add(la_content);
		p_content.add(content);
		p_content.add(bt_back);
		p_content.add(bt_edit);
		p_content.add(bt_remove);

		add(p_content);

		bt_back.addActionListener((e) -> {
			getMyCalendarMain().showPage(MyCalendarMain.HOME);
		});

		bt_edit.addActionListener((e) -> {
			int ans = JOptionPane.showConfirmDialog(this, "수정 하시겠습니까?");

			if (ans == JOptionPane.OK_OPTION) {
				int result = edit(contentvo);
				if (result == 0) {
					JOptionPane.showMessageDialog(ContentDetail.this, "수정 실패");
				} else {
					JOptionPane.showMessageDialog(ContentDetail.this, "일정 수정 성공");
					getMyCalendarMain().showPage(MyCalendarMain.HOME);
					getMyCalendarMain().tableUpdate();
				}
			}
		});

		bt_remove.addActionListener((e) -> {
			int ans = JOptionPane.showConfirmDialog(this, "삭제 하시겠습니까?");

			if (ans == JOptionPane.OK_OPTION) {
				int result = del(contentvo.getContent_id());
				if (result == 0) {
					JOptionPane.showMessageDialog(ContentDetail.this, "삭제 실패");
				} else {
					JOptionPane.showMessageDialog(ContentDetail.this, "일정 삭제 성공");
					getMyCalendarMain().showPage(MyCalendarMain.HOME);
					getMyCalendarMain().tableUpdate();
				}
			}
		});
	}

	public int edit(ContentVO contentvo) {
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "update content set regdate=? , title=? , content=? where content_id=?";

		try {
			pstmt = getMyCalendarMain().getCon().prepareStatement(sql);
			pstmt.setString(1, t_regdate.getText());
			pstmt.setString(2, t_title.getText());
			pstmt.setString(3, area_content.getText());
			pstmt.setInt(4, contentvo.getContent_id());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getMyCalendarMain().getDbManager().close(pstmt);
		}
		return result;
	}

	public int del(int content_id) {
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "delete from content where content_id="+content_id;

		try {
			pstmt = getMyCalendarMain().getCon().prepareStatement(sql);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getMyCalendarMain().getDbManager().close(pstmt);
		}
		return result;
	}

	public void getDetail(int content_id) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select content_id,member_id,title,content,to_char(regdate,'yyyy-mm-dd') as regdate from content where content_id=" + content_id;

		try {
			pstmt = getMyCalendarMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				contentvo = new ContentVO();
				contentvo.setContent_id(rs.getInt("content_id"));
				contentvo.setMember_id(rs.getInt("member_id"));
				contentvo.setRegdate(rs.getString("regdate"));
				contentvo.setTitle(rs.getString("title"));
				contentvo.setContent(rs.getString("content"));

				t_title.setText(contentvo.getTitle());
				t_regdate.setText(contentvo.getRegdate());
				area_content.setText(contentvo.getContent());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getMyCalendarMain().getDbManager().close(pstmt);
		}

	}
}
