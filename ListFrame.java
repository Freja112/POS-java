package swing_design;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ListFrame extends JDialog implements ActionListener {
	JTable table;
	ObjectModel oModel;
	JButton bt_insert, bt_update, bt_delete, bt_search, bt_default;
	JTextField text_search;
	JComboBox combo;
	String strCb;
	UpdateDialog uDialog;
	InsertDialog iDialog;
	Box box;
	
	GridBagConstraints c;
	
	public ListFrame() {
		c = new GridBagConstraints();
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		
		this.setLayout(new GridBagLayout());
		
		strCb = "상품명";
		
		
		/*콤포넌트 초기화*/
		oModel = new ObjectModel();
		table = new JTable(oModel);
		String[] str_combo = { "상품명", "제조사", "바코드 넘버"};
		combo = new JComboBox(str_combo);		
		text_search = new JTextField();
		bt_insert = new JButton("상품 등록");
		bt_update = new JButton("상품 정보 변경");
		bt_delete = new JButton("선택 상품 삭제");
		bt_search = new JButton("검색");
		bt_default = new JButton("전체 목록 보기");
		box = Box.createHorizontalBox();
		
		
		
		
		/*콤포넌트 설정*/
		combo.addActionListener(this);
		text_search.addActionListener(this);
		bt_insert.addActionListener(this);
		bt_update.addActionListener(this);
		bt_delete.addActionListener(this);
		bt_search.addActionListener(this);
		bt_default.addActionListener(this);
		table.getTableHeader().setReorderingAllowed(false);
		
		
		
		
		/*SQL문 : 상품 목록 로드*/
		List<Map<String, String>> allData = Dao.getinstance().executeDBList("select_object_list", null);
		oModel.setValueAll(allData);
		
		
		
		
		/*콤포넌트 배치*/
		box.add(table);
		box.add(new JScrollPane(table));
		c.weighty = 0;
		layout(text_search, 1, 0, 1, 1);
		c.weightx = 0;
		layout(combo, 0, 0, 1, 1);
		layout(bt_search, 2, 0, 1, 1);
		layout(bt_default, 3, 0, 1, 1);
		c.weightx = 1;
		c.weighty = 1;
		layout(box, 0, 1, 3, 3);
		layout(bt_insert, 3, 1, 1, 1);
		layout(bt_update, 3, 2, 1, 1);
		layout(bt_delete, 3, 3, 1, 1);
		


		this.setSize(800, 600);
		this.setModal(true);
		this.setVisible(true);
	}
	
	public void layout(Component obj, int x, int y, int width, int height) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		add(obj, c);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == bt_search || source == text_search) {		 // 검색 버튼 리스너
			List<Map<String, String>> getData = null;
			List<String> params = new ArrayList<String>();
			params.add(text_search.getText());
			
			if(strCb == "상품명") {
				getData = Dao.getinstance().executeDBList("select_object_name", params);
			} else if(strCb == "제조사") {
				getData = Dao.getinstance().executeDBList("select_object_company", params);
			} else if(strCb == "바코드 넘버"){
				getData = Dao.getinstance().executeDBList("select_object_serial", params);
			}
			
			oModel.clearTable();
			oModel.setValueAll(getData);

			text_search.setText("");
		} else if (source == bt_insert) { 		// 상품 등록 버튼 리스너
			iDialog = new InsertDialog(oModel);
		} else if (source == bt_update) { 		// 상품 정보 변경 버튼 리스너
			int row = table.getSelectedRow();
			uDialog = new UpdateDialog((String)oModel.getValueAt(row, 0), (String)oModel.getValueAt(row, 1), (String)oModel.getValueAt(row, 2), (String)oModel.getValueAt(row, 3), oModel);
		} else if (source == bt_delete) { 		// 선택 상품 삭제 버튼 리스너
			int row = table.getSelectedRow();
			List<String> params = new ArrayList<String>();
			params.add((String)oModel.getValueAt(row, 3));

			Dao.getinstance().executeDBUpdate("delete_object", params);
			
			oModel.removeAtIndexRow(row);
		} else if (source == combo) {		// 콤보 박스 리스너
			JComboBox cb = (JComboBox)e.getSource();
			strCb = (String)cb.getSelectedItem();
		} else if (source == bt_default){		// 전체 목록 보기 버튼 리스너
			oModel.clearTable();
			List<Map<String, String>> allData = Dao.getinstance().executeDBList("select_object_list", null);
			oModel.setValueAll(allData);
		}
	}
}
