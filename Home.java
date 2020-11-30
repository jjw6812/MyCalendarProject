package mycalendar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;

public class Home extends Page {
	JPanel p_west;
	JPanel p_center, p_center2, p_north, p_south;
	JTable table;
	JScrollPane s2;
	JButton bt_regist, bt_search;
	JButton bt_logout, bt_total;
	JTextField t_year, t_content, t_date;
	TableModel model;
	MyCalendarMain myCalendarMain;
	DrawCalendar calendar;
	String selectedYY;
	String selectedMM;
	String selectedDD;
	String date;
	String date2;
	ContentRegist regist;
	String yy;
	String mm;
	String dd;
	Calendar cal;
	
	public Home(MyCalendarMain myCalendarMain) {
		super(myCalendarMain);
		this.myCalendarMain = myCalendarMain;
		p_west = new JPanel();
		p_center = new JPanel();
		p_center2 = new JPanel();
		p_north = new JPanel();
		p_south = new JPanel();
		table = new JTable();
		s2 = new JScrollPane(table);
		bt_regist = new JButton("등록하기");
		bt_search = new JButton("검색");
		bt_logout = new JButton("로그아웃");
		bt_total = new JButton("전체목록");
		t_content = new JTextField(25);
		calendar = new DrawCalendar(this);
		t_date = new JTextField(10);

		p_west.setPreferredSize(new Dimension(400, 610));
		s2.setPreferredSize(new Dimension(580, 350));

		setLayout(new BorderLayout());
		p_west.setLayout(new BorderLayout());
		p_center.setLayout(new BorderLayout());

		p_west.add(calendar);
		p_center.add(p_north, BorderLayout.NORTH);
		p_center.add(p_south, BorderLayout.SOUTH);
		p_center.add(p_center2, BorderLayout.CENTER);
		p_north.add(t_date);
		p_north.add(t_content);
		p_north.add(bt_search);
		p_north.add(bt_logout);
		p_center2.add(s2);
		p_south.add(bt_total);
		p_south.add(bt_regist);
		
		cal = Calendar.getInstance();
		yy = Integer.toString(cal.get(Calendar.YEAR));
		mm = Integer.toString(cal.get(Calendar.MONTH)+1);
		dd = Integer.toString(cal.get(Calendar.DATE));
		t_date.setText(yy+"-"+mm+"-"+dd);
		
		add(p_west, BorderLayout.WEST);
		add(p_center, BorderLayout.CENTER);
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				ContentDetail contentDetail = (ContentDetail) myCalendarMain.getPage(MyCalendarMain.CONTENTDETAIL);
				String content_id = (String) table.getValueAt(table.getSelectedRow(), 0);
				contentDetail.getDetail(Integer.parseInt(content_id));
				getMyCalendarMain().showPage(MyCalendarMain.CONTENTDETAIL);
			}
		});

		bt_regist.addActionListener((e) -> {
			regist = (ContentRegist) myCalendarMain.getPage(myCalendarMain.CONTENTREGIST);
			regist.t_id.setText(myCalendarMain.getMember().getName());
			getMyCalendarMain().showPage(MyCalendarMain.CONTENTREGIST);
			getMyCalendarMain().contentRegistInit();
			if (selectedDD != null) {
				date = selectedYY +"-"+ selectedMM +"-"+ selectedDD;
				date2 = selectedYY+selectedMM+selectedDD;
				regist.t_regdate.setText(date);
				regist.updateUI();
			} else {
				date = yy+"-"+mm+"-"+dd;
				date2 = yy+mm+dd;
				regist.t_regdate.setText(date);
				regist.updateUI();
			}
		});
		
		bt_search.addActionListener((e)->{
			getContent2();
		});
		
		bt_logout.addActionListener((e) -> {
			getMyCalendarMain().showPage(MyCalendarMain.LOGIN);
			getMyCalendarMain().loginInit();
		});
		
		bt_total.addActionListener((e) ->{
			getContent();
		});
	}

	public void getContent() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
			sql = "select content_id as 번호, title as 제목, content as 내용, to_char(regdate,'yyyy-mm-dd') as 날짜 from content where member_id="
					+ myCalendarMain.getMember().getMember_id() +" order by content_id desc";
		try {
			pstmt = getMyCalendarMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();

			ResultSetMetaData meta = rs.getMetaData();
			ArrayList<String> columnNames = new ArrayList<String>();

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				String colName = meta.getColumnName(i);
				columnNames.add(colName);
			}

			ArrayList<ContentVO> contentList = new ArrayList<ContentVO>();

			while (rs.next()) {
				ContentVO vo = new ContentVO();

				vo.setContent_id((rs.getInt("번호")));
				// vo.setMember_id((rs.getInt("member_id")));
				vo.setTitle((rs.getString("제목")));
				vo.setContent((rs.getString("내용")));
				vo.setRegdate(rs.getString("날짜"));
				contentList.add(vo);

			}

			model = new TableModel();
			model.column = columnNames;
			model.record = contentList;
			table.setModel(model);
			table.updateUI();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getMyCalendarMain().getDbManager().close(pstmt, rs);
		}
	}
	
	public void getContent2() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		if(t_content.getText().equals("")) {
			sql = "select content_id as 번호, title as 제목, content as 내용, to_char(regdate,'yyyy-mm-dd') as 날짜 from content where member_id="
					+ myCalendarMain.getMember().getMember_id() +" and regdate=\'"+getSelectedYY()+getSelectedMM()+getSelectedDD()+"\' order by content_id desc";
		
		}else {
			sql = "select content_id as 번호, title as 제목, content as 내용, to_char(regdate,'yyyy-mm-dd') as 날짜 from content where member_id="
					+ myCalendarMain.getMember().getMember_id() +" and regdate=\'"+getSelectedYY()+getSelectedMM()+getSelectedDD()+"\' and title like \'%"+t_content.getText()+"%\' order by content_id desc";
		}
		try {
			pstmt = getMyCalendarMain().getCon().prepareStatement(sql);
			rs = pstmt.executeQuery();

			ResultSetMetaData meta = rs.getMetaData();
			ArrayList<String> columnNames = new ArrayList<String>();

			for (int i = 1; i <= meta.getColumnCount(); i++) {
				String colName = meta.getColumnName(i);
				columnNames.add(colName);
			}

			ArrayList<ContentVO> contentList = new ArrayList<ContentVO>();

			while (rs.next()) {
				ContentVO vo = new ContentVO();

				vo.setContent_id((rs.getInt("번호")));
				// vo.setMember_id((rs.getInt("member_id")));
				vo.setTitle((rs.getString("제목")));
				vo.setContent((rs.getString("내용")));
				vo.setRegdate(rs.getString("날짜"));
				contentList.add(vo);

			}

			model = new TableModel();
			model.column = columnNames;
			model.record = contentList;
			table.setModel(model);
			table.updateUI();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			getMyCalendarMain().getDbManager().close(pstmt, rs);
		}
	}

	public void tableUpdate() {
		table.updateUI();
	}

	public void setSelectedYY(String selectedYY) {
		this.selectedYY = selectedYY;
	}

	public void setSelectedMM(String selectedMM) {
		this.selectedMM = selectedMM;
	}

	public void setSelectedDD(String selectedDD) {
		this.selectedDD = selectedDD;
	}

	public String getSelectedYY() {
		return selectedYY;
	}

	public DrawCalendar getCalendar() {
		return calendar;
	}

	public void setCalendar(DrawCalendar calendar) {
		this.calendar = calendar;
	}

	public String getSelectedMM() {
		return selectedMM;
	}

	public String getSelectedDD() {
		return selectedDD;
	}
}
