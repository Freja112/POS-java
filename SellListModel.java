package swing_design;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class SellListModel extends AbstractTableModel {			// ObjectModel.java�� ������ ����, �ݷ��� ������ �ϳ� �� ����

	Object[] columnNames = { "��ǰ��", "������", "����", "���ڵ� �ѹ�", "�ŷ� ����" };
	List<List<String>> data = new ArrayList<List<String>>();

	public String getColumnName(int col) {
		return columnNames[col].toString();
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).get(columnIndex);
	}	

	public void setValueRow(List<String> sValue) {
		data.add(sValue);
		fireTableDataChanged();
	}
	
	public void setValueAll(List<Map<String, String>> Value) {
		for(int i=0; i<Value.size(); i++){
			List<String> temp = new ArrayList<String>();
			temp.add(Value.get(i).get("name"));
			temp.add(Value.get(i).get("company"));
			temp.add(Value.get(i).get("price"));
			temp.add(Value.get(i).get("serial"));
			temp.add(Value.get(i).get("date"));
			data.add(temp);
		}
		fireTableDataChanged();
	}
	
	public void clearTable(){
		data.clear();
	}

	public void removeAtIndexRow(int row) {
		data.remove(row);
		fireTableDataChanged();
	}
}
