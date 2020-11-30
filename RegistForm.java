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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegistForm extends Page {
	JPanel p_content;
	JTextField t_id;
	JPasswordField t_pass;
	JTextField t_name;
	JTextField t_phone;
	JTextField t_email;
	JButton bt_regist, bt_back;
	JLabel la_title;
	JLabel la_id, la_pass, la_name, la_phone, la_email;

	public RegistForm(MyCalendarMain myCalendarMain) {
		super(myCalendarMain);
		p_content = new JPanel();

		t_id = new JTextField();
		t_pass = new JPasswordField();
		t_name = new JTextField();
		t_phone = new JTextField();
		t_email = new JTextField();
		la_title = new JLabel("회원가입");
		la_id = new JLabel("아이디");
		la_pass = new JLabel("비밀번호");
		la_name = new JLabel("이름");
		la_phone = new JLabel("연락처");
		la_email = new JLabel("이메일");
		bt_regist = new JButton("회원가입");
		bt_back = new JButton("뒤로가기");

		p_content.setPreferredSize(new Dimension(400, 370));
		p_content.setBackground(Color.WHITE);
		Dimension d = new Dimension(340, 25);
		t_id.setPreferredSize(d);
		t_pass.setPreferredSize(d);
		t_name.setPreferredSize(d);
		t_phone.setPreferredSize(d);
		t_email.setPreferredSize(d);
		Dimension d2 = new Dimension(30, 25);
		la_id.setPreferredSize(d);
		la_pass.setPreferredSize(d);
		la_name.setPreferredSize(d);
		la_phone.setPreferredSize(d);
		la_email.setPreferredSize(d);
		la_title.setPreferredSize(new Dimension(370, 25));
		la_title.setHorizontalAlignment(JLabel.CENTER);
		la_title.setFont(new Font(null,Font.BOLD,25));

		p_content.add(la_title);
		p_content.add(la_id);
		p_content.add(t_id);
		p_content.add(la_pass);
		p_content.add(t_pass);
		p_content.add(la_name);
		p_content.add(t_name);
		p_content.add(la_phone);
		p_content.add(t_phone);
		p_content.add(la_email);
		p_content.add(t_email);		
		p_content.add(bt_back);
		p_content.add(bt_regist);

		add(p_content);

		bt_back.addActionListener((e)->{
			getMyCalendarMain().showPage(MyCalendarMain.LOGIN);
			getMyCalendarMain().loginInit();
		});
		
		bt_regist.addActionListener((e) -> {

			if (checkId(t_id.getText())) {
				JOptionPane.showMessageDialog(RegistForm.this, "중복된 아이디입니다\n다른 아이디를 사용하세요");
			} else {
				Member vo = new Member();
				vo.setId(t_id.getText());
				vo.setPass(new String(t_pass.getPassword()));
				vo.setName(t_name.getText());
				vo.setPhone(t_phone.getText());
				vo.setEmail(t_email.getText());

				int result = regist(vo);

				if (result == 0) {
					JOptionPane.showMessageDialog(RegistForm.this, "등록 실패");
				} else {
					JOptionPane.showMessageDialog(RegistForm.this, "회원가입 성공");
					getMyCalendarMain().showPage(MyCalendarMain.LOGIN);
					getMyCalendarMain().loginInit();
				}
			}

		});
	}

	public boolean checkId(String id) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;

		String sql = "select * from member where id=?";

		try {
			pstmt = getMyCalendarMain().getCon().prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			flag = rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getMyCalendarMain().getDbManager().close(pstmt, rs);
		}
		return flag;
	}

	public int regist(Member member) {
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "insert into member(member_id,id,pass,name,phone,email)";
		sql += " values(seq_member.nextval,?,?,?,?,?)";

		try {
			pstmt = getMyCalendarMain().getCon().prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPass());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getPhone());
			pstmt.setString(5, member.getEmail());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getMyCalendarMain().getDbManager().close(pstmt);
		}
		return result;
	}
	
	public void init() {
		t_id.setText("");
		t_pass.setText("");
		t_name.setText("");
		t_phone.setText("");
		t_email.setText("");
	}
}
