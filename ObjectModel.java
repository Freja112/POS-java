package swing_design;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class ObjectModel extends AbstractTableModel {

	Object[] columnNames = { "��ǰ��", "������", "����", "���ڵ� �ѹ�" };
	List<List<String>> data = new ArrayList<List<String>>();

	public String getColumnName(int col) {	// �ش� �ݷ��� ��Ī�� ���ڿ��� ��ȯ
		return columnNames[col].toString();
	}

	public boolean isCellEditable(int row, int col) {	// ���̺� ���� ��Ȱ��ȭ
		return false;
	}

	@Override
	public int getRowCount() {	// �� ���� ��ȯ
		return data.size();
	}

	@Override
	public int getColumnCount() {	// �ݷ� ���� ��ȯ
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).get(columnIndex);
	}

	public void setValueRow(List<String> sValue) {	// �� �Ѱ� ����
		data.add(sValue);		
		fireTableDataChanged();
	}
	
	public void setValueAll(List<Map<String, String>> Value) {	// 2���迭�� �޾Ƽ� ����
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
	
	public void clearTable(){	// ���̺� �� �ʱ�ȭ
		data.clear();
		fireTableDataChanged();
	}

	public void removeAtIndexRow(int row) {	// �ش� �ε����� �� �� ����
		data.remove(row);
		fireTableDataChanged();
	}
}
