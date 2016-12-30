package swing_design;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class SellListModel extends AbstractTableModel {			// ObjectModel.java와 동일한 구조, 콜롬의 갯수가 하나 더 많음

	Object[] columnNames = { "상품명", "제조사", "가격", "바코드 넘버", "거래 일자" };
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
