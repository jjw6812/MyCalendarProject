package mycalendar;

import java.awt.Dimension;

import javax.swing.JPanel;

public class Page extends JPanel{
	private MyCalendarMain myCalendarMain;
	
	public MyCalendarMain getMyCalendarMain() {
		return myCalendarMain;
	}
	
	public Page(MyCalendarMain myCalendarMain) {
		this.myCalendarMain=myCalendarMain;
		this.setPreferredSize(new Dimension(MyCalendarMain.WIDTH-50, MyCalendarMain.HEIGHT-60));
	}
	
}