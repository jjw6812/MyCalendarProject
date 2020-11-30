package mycalendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends Page{
	JPanel p_content;
	JTextField t_id;
	JPasswordField t_pass;
	JButton bt_login;
	JButton bt_regist;
	JLabel la_title;
	JLabel la_id;
	JLabel la_pass;
	MyCalendarMain myCalendarMain;

	public Login(MyCalendarMain myCalendarMain) {
		super(myCalendarMain);
		this.myCalendarMain = myCalendarMain;
		p_content = new JPanel();
		t_id = new JTextField();
		t_pass = new JPasswordField();
		la_title = new JLabel("로그인");
		la_id = new JLabel("아이디");
		la_pass = new JLabel("비밀번호");
		bt_login = new JButton("로그인");
		bt_regist = new JButton("회원가입");
		
		p_content.setPreferredSize(new Dimension(380, 140));
		p_content.setBackground(Color.WHITE);
		t_id.setPreferredSize(new Dimension(250, 25));
		t_pass.setPreferredSize(new Dimension(250, 25));
		la_title.setPreferredSize(new Dimension(370, 35));
		la_title.setHorizontalAlignment(JLabel.CENTER);
		la_title.setFont(new Font(null,Font.BOLD,25));
		la_id.setPreferredSize(new Dimension(60, 25));
		la_pass.setPreferredSize(new Dimension(60, 25));
		
		p_content.add(la_title);
		p_content.add(la_id);
		p_content.add(t_id);
		p_content.add(la_pass);
		p_content.add(t_pass);
		p_content.add(bt_login);
		p_content.add(bt_regist);
		
		add(p_content);
		
		t_id.requestFocus();
		
		bt_regist.addActionListener((e)->{
			getMyCalendarMain().showPage(MyCalendarMain.MEMBER_REGIST);
			getMyCalendarMain().memberRegistInit();
		});
		
		bt_login.addActionListener((e)->{
			Member vo = new Member();
			vo.setId(t_id.getText());
			vo.setPass(new String(t_pass.getPassword()));
			
			validCheck(vo);
		});
		
		t_id.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					Member vo = new Member();
					vo.setId(t_id.getText());
					vo.setPass(new String(t_pass.getPassword()));
					
					validCheck(vo);
				}
			}
		});
		
		t_pass.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					Member vo = new Member();
					vo.setId(t_id.getText());
					vo.setPass(new String(t_pass.getPassword()));
					
					validCheck(vo);
				}
			}
		});
	}
	
	public void validCheck(Member member) {
		if(member.getId().length()<1) {
			JOptionPane.showMessageDialog(this, "아이디를 입력하세요");
		}else if(member.getPass().length()<1) {
			JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요");
		}else {
			if(login(member)==null) {
				JOptionPane.showMessageDialog(this, "로그인 정보가 올바르지 않습니다");
				init();
			}else {
				JOptionPane.showMessageDialog(this, "로그인 성공");
				getMyCalendarMain().showPage(MyCalendarMain.HOME);
			}
		}
	}
	
	public Member login(Member member) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Member vo= null;
		
		String sql="select * from member ";
		sql+=" where id=? and pass=?";
		try {
			pstmt=getMyCalendarMain().getCon().prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPass());
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				vo = new Member();
				
				vo.setMember_id(rs.getInt("member_id"));
				vo.setId(rs.getString("id"));
				vo.setPass(rs.getString("pass"));
				vo.setName(rs.getString("name"));
				vo.setPhone(rs.getString("phone"));
				vo.setEmail(rs.getString("email"));
				
				myCalendarMain.setMember(vo);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getMyCalendarMain().getDbManager().close(pstmt, rs);
		}
		return vo;
	}
	
	public void init() {
		t_id.setText("");
		t_pass.setText("");
	}
}

















