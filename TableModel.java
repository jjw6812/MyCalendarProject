package mycalendar;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel{
	ArrayList<ContentVO> record = new ArrayList<ContentVO>();
	ArrayList<String> column = new ArrayList<>();
	
	public int getRowCount() {
		return record.size();
	}

	public int getColumnCount() {
		return column.size();
	}
	
	public String getColumnName(int col) {
		return column.get(col);
	}

	public Object getValueAt(int row, int col) {
		ContentVO vo = record.get(row);
		String obj=null;
		if(col==0) {
			obj = Integer.toString(vo.getContent_id());
		}else if(col==1) {
			obj = vo.getTitle();
		}else if(col==2) {
			obj = vo.getContent();
		}else if(col==3) {
			obj = vo.getRegdate();
		}
		return obj;
	}
}
