package swing_design;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class ObjectModel extends AbstractTableModel {

	Object[] columnNames = { "상품명", "제조사", "가격", "바코드 넘버" };
	List<List<String>> data = new ArrayList<List<String>>();

	public String getColumnName(int col) {	// 해당 콜롬의 명칭을 문자열로 반환
		return columnNames[col].toString();
	}

	public boolean isCellEditable(int row, int col) {	// 테이블 수정 비활성화
		return false;
	}

	@Override
	public int getRowCount() {	// 열 갯수 반환
		return data.size();
	}

	@Override
	public int getColumnCount() {	// 콜롬 갯수 반환
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).get(columnIndex);
	}

	public void setValueRow(List<String> sValue) {	// 열 한개 삽입
		data.add(sValue);		
		fireTableDataChanged();
	}
	
	public void setValueAll(List<Map<String, String>> Value) {	// 2차배열로 받아서 삽입
		for(int i=0; i<Value.size(); i++){
			List<String> temp = new ArrayList<String>();
			temp.add(Value.get(i).get("name"));
			temp.add(Value.get(i).get("company"));
			temp.add(Value.get(i).get("price"));
			temp.add(Value.get(i).get("serial"));
			data.add(temp);
		}
		fireTableDataChanged();
	}
	
	public void clearTable(){	// 테이블 값 초기화
		data.clear();
		fireTableDataChanged();
	}

	public void removeAtIndexRow(int row) {	// 해당 인덱스의 열 값 제거
		data.remove(row);
		fireTableDataChanged();
	}
}
