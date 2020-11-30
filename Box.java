package mycalendar;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Box extends Canvas{
	String title;
	int width;
	int height;
	
	public Box(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.BLACK);
		g.drawString(title, 2, 20);
	}
	
	public String getTitle() {
		return title;
	}
}
