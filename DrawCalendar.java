package mycalendar;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JPanel;

public class DrawCalendar extends JPanel {
	JPanel p_top;
	JPanel p_center;
	Choice ch_yy;
	Choice ch_mm;
	JButton bt;
	Box[] box = new Box[7 * 6];
	int width = 50;
	int height = 50;
	int startDayOfWeek;
	int lastDate;
	int startMonth;
	Home home;
	public DrawCalendar(Home home) {
		this.home = home;
		p_top = new JPanel();
		p_center = new JPanel();
		ch_yy = new Choice();
		ch_mm = new Choice();
		bt = new JButton("확인");

		this.setLayout(new BorderLayout());
		p_center.setLayout(new GridLayout(0, 7));

		p_center.setPreferredSize(new Dimension(300, 500));

		initDate();
		createTitle();
		getStartDayOfWeek();
		getLastDate();
		createDateBox();

		bt.addActionListener((e) -> {
			getStartDayOfWeek();
			getLastDate();
			printData();
		});

		p_top.add(ch_yy);
		p_top.add(ch_mm);
		p_top.add(bt);

		this.add(p_top, BorderLayout.NORTH);
		this.add(p_center);

	}

	public void initDate() {
		Calendar cal = Calendar.getInstance();
		int yy = cal.get(Calendar.YEAR);
		int mm = cal.get(Calendar.MONTH) + 1;

		for (int i = 2030; i >= 1990; i--) {
			ch_yy.add(Integer.toString(i));
		}
		ch_yy.select(Integer.toString(yy));

		for (int i = 1; i <= 12; i++) {
			ch_mm.add(Integer.toString(i));
		}
		ch_mm.select(Integer.toString(mm));
	}

	public void createTitle() {
		for (int i = 0; i < 7; i++) {
			box[0] = new Box("일", width, height);
			box[1] = new Box("월", width, height);
			box[2] = new Box("화", width, height);
			box[3] = new Box("수", width, height);
			box[4] = new Box("목", width, height);
			box[5] = new Box("금", width, height);
			box[6] = new Box("토", width, height);
			box[i].setPreferredSize(new Dimension(width, height));
			p_center.add(box[i]);
		}
	}

	public void createDateBox() {
		Calendar cal = Calendar.getInstance();
		
		int yy = Integer.parseInt(ch_yy.getSelectedItem());
		int mm = Integer.parseInt(ch_mm.getSelectedItem());
		
		cal.set(yy, mm - 1, 1);
		startMonth = cal.get(Calendar.DAY_OF_WEEK);

		for (int a = 1; a < startMonth; a++) {
			box[a] = new Box("", width, height);
			box[a].setPreferredSize(new Dimension(width, height));
			p_center.add(box[a]);
		}

		for (int i = (startDayOfWeek); i <= lastDate; i++) {
			box[i] = new Box(Integer.toString(i), width, height);
			box[i].setPreferredSize(new Dimension(width, height));
			box[i].addMouseListener(new MouseAdapter() {
				public void mouseReleased(java.awt.event.MouseEvent e) {
					Box box = (Box)e.getSource();
					String title = box.getTitle();
					home.setSelectedYY(ch_yy.getSelectedItem());
					home.setSelectedMM(ch_mm.getSelectedItem());
					if(Integer.parseInt(title)<10) {
						home.setSelectedDD("0"+title);
						home.t_date.setText(ch_yy.getSelectedItem()+"-"+ch_mm.getSelectedItem()+"-0"+title);
					}else {
						home.setSelectedDD(title);
						home.t_date.setText(ch_yy.getSelectedItem()+"-"+ch_mm.getSelectedItem()+"-"+title);
					}
				}
			});
			p_center.add(box[i]);
		}

		for (int a = 1; a < 42 - lastDate - startMonth + 2; a++) {
			box[a] = new Box("", width, height);
			box[a].setPreferredSize(new Dimension(width, height));
			p_center.add(box[a]);
		}
		
	}

	public void getStartDayOfWeek() {
		Calendar cal = Calendar.getInstance();

		int yy = Integer.parseInt(ch_yy.getSelectedItem());
		int mm = Integer.parseInt(ch_mm.getSelectedItem());

		cal.set(yy, mm, 1);

		startDayOfWeek = cal.get(Calendar.DAY_OF_MONTH);
	}

	public void getLastDate() {
		Calendar cal = Calendar.getInstance();

		int yy = Integer.parseInt(ch_yy.getSelectedItem());
		int mm = Integer.parseInt(ch_mm.getSelectedItem());

		cal.set(yy, mm, 0);

		lastDate = cal.get(Calendar.DATE);
	}

	public void printData() {
		Calendar cal = Calendar.getInstance();

		int yy = Integer.parseInt(ch_yy.getSelectedItem());
		int mm = Integer.parseInt(ch_mm.getSelectedItem());

		cal.set(yy, mm - 1, 1);
		startMonth = cal.get(Calendar.DAY_OF_WEEK);

		p_center.removeAll();
		createTitle();

		for (int a = 1; a < startMonth; a++) {
			box[a] = new Box("", width, height);
			box[a].setPreferredSize(new Dimension(width, height));
			p_center.add(box[a]);
		}

		for (int i = (startDayOfWeek); i <= lastDate; i++) {
			box[i] = new Box(Integer.toString(i), width, height);
			box[i].setPreferredSize(new Dimension(width, height));
			box[i].addMouseListener(new MouseAdapter() {
				public void mouseReleased(java.awt.event.MouseEvent e) {
					Box box = (Box)e.getSource();
					String title = box.getTitle();
					home.setSelectedYY(ch_yy.getSelectedItem());
					home.setSelectedMM(ch_mm.getSelectedItem());
					if(Integer.parseInt(title)<10) {
						home.setSelectedDD("0"+title);
						home.t_date.setText(ch_yy.getSelectedItem()+"-"+ch_mm.getSelectedItem()+"-0"+title);
					}else {
						home.setSelectedDD(title);
						home.t_date.setText(ch_yy.getSelectedItem()+"-"+ch_mm.getSelectedItem()+"-"+title);
					}
				}
			});
			p_center.add(box[i]);
		}

		for (int a = 1; a < 42 - lastDate - startMonth + 2; a++) {
			box[a] = new Box("", width, height);
			box[a].setPreferredSize(new Dimension(width, height));
			p_center.add(box[a]);
		}

		this.updateUI();
	}
}
