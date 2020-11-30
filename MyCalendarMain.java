package mycalendar;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyCalendarMain extends JFrame {
	public static final int WIDTH = 1050;
	public static final int HEIGHT = 500;

	public static final int LOGIN = 0;
	public static final int MEMBER_REGIST = 1;
	public static final int HOME = 2;
	public static final int CONTENTREGIST = 3;
	public static final int CONTENTDETAIL = 4;

	JPanel user_container;
	JPanel p_content;
	JPanel p_navi;

	Page[] page = new Page[5];

	DBManager dbManager;
	Connection con;
	
	private Member member;
	
	private DrawCalendar drawCalendar;
	private Box box;
	
	
	public MyCalendarMain() {
		dbManager = new DBManager();
		user_container = new JPanel();
		p_content = new JPanel();
		p_navi = new JPanel();

		con = dbManager.connect();
		if (con == null) {
			JOptionPane.showMessageDialog(this, "데이터베이스에 접속할 수 없습니다");
		} else {
			this.setTitle("MyCalendar");
		}

		page[LOGIN] = new Login(this);
		page[MEMBER_REGIST] = new RegistForm(this);
		page[HOME] = new Home(this);
		page[CONTENTREGIST] = new ContentRegist(this);
		page[CONTENTDETAIL] = new ContentDetail(this);

		user_container.setPreferredSize(new Dimension(WIDTH - 100, HEIGHT - 120));
		user_container.setBackground(Color.WHITE);

		user_container.setLayout(new BorderLayout());
		user_container.add(p_navi, BorderLayout.CENTER);

		for (int i = 0; i < page.length; i++) {
			p_content.add(page[i]);
		}

		user_container.add(p_content);

		this.add(user_container);

		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setLocationRelativeTo(null);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dbManager.disConnect(con);
				System.exit(0);
			}
		});

		showPage(0);
	}

	public void showPage(int showIndex) {
		for (int i = 0; i < page.length; i++) {
			if(showIndex==HOME) {
				((Home)page[HOME]).getContent();
			}
			if (i == showIndex) {
				page[i].setVisible(true);
			} else {
				page[i].setVisible(false);
			}
		}
	}

	public DBManager getDbManager() {
		return dbManager;
	}

	public Connection getCon() {
		return con;
	}

	public void tableUpdate() {
		((Home)page[HOME]).getContent();
	}
	
	public void contentRegistInit() {
		((ContentRegist)page[CONTENTREGIST]).init();
	}
	
	public void memberRegistInit() {
		((RegistForm)page[MEMBER_REGIST]).init();
	}
	
	public void loginInit() {
		((Login)page[LOGIN]).init();
	}
	
	public Member getMember() {
		return member;
	}
	
	public void setMember(Member member) {
		this.member = member;
	}
	
	public Page getPage(int pageName) {
		return page[pageName];
	}
	
	public static void main(String[] args) {
		new MyCalendarMain();
	}

	public DrawCalendar getDrawCalendar() {
		return drawCalendar;
	}

	public void setDrawCalendar(DrawCalendar drawCalendar) {
		this.drawCalendar = drawCalendar;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	
	
	
}
