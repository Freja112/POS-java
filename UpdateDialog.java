package swing_design;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class UpdateDialog extends JDialog implements ActionListener {
	GridBagConstraints c;
	JLabel lb_name, lb_company, lb_price, lb_serial;
	JTextField tf_name, tf_company, tf_price, tf_serial;
	JButton bt_commit, bt_cancel;
	String str_preSerial;
	ObjectModel repModel;
	
	public UpdateDialog(String name, String company, String price, String serial, ObjectModel oModel) {
		c = new GridBagConstraints();
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		this.setLayout(new GridBagLayout());
		
		str_preSerial = serial;
		repModel = oModel;
		
		
		/*콤포넌트 초기화*/
		lb_name = new JLabel("상품명");
		lb_company = new JLabel("제조사");
		lb_price = new JLabel("가격");
		lb_serial = new JLabel("바코드 넘버");
		tf_name = new JTextField(name);
		tf_company = new JTextField(company);
		tf_price = new JTextField(price);
		tf_serial = new JTextField(serial);
		bt_commit = new JButton("수정");
		bt_cancel = new JButton("취소");
		
		
		
		/*콤포넌트 설정*/
		lb_name.setHorizontalAlignment(JLabel.CENTER);
		lb_company.setHorizontalAlignment(JLabel.CENTER);
		lb_price.setHorizontalAlignment(JLabel.CENTER);
		lb_serial.setHorizontalAlignment(JLabel.CENTER);
		bt_commit.addActionListener(this);
		bt_cancel.addActionListener(this);
		
		
		
		/*콤포넌트 배치*/
		layout(tf_name, 1, 0, 3, 1);
		layout(tf_company, 1, 1, 3, 1);
		layout(tf_price, 1, 2, 3, 1);
		layout(tf_serial, 1, 3, 3, 1);
		layout(bt_commit, 0, 4, 2, 1);
		layout(bt_cancel, 2, 4, 2, 1);
		c.weightx = 0;
		layout(lb_name, 0, 0, 1, 1);
		layout(lb_company, 0, 1, 1, 1);
		layout(lb_price, 0, 2, 1, 1);
		layout(lb_serial, 0, 3, 1, 1);
		
		
		
		
		this.setSize(400, 200);
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
		
		if (source == bt_commit) { // 수정 버튼 리스너
			List<String> params = new ArrayList<String>();
			params.add(tf_name.getText());
			params.add(tf_company.getText());
			params.add(tf_price.getText());
			params.add(tf_serial.getText());
			params.add(str_preSerial);

			Dao.getinstance().executeDBUpdate("update_object", params);
			
			repModel.clearTable();
			List<Map<String, String>> allData = Dao.getinstance().executeDBList("select_object_list", null);
			repModel.setValueAll(allData);
			
			dispose();
		} else if (source == bt_cancel) { // 취소 버튼 리스너
			dispose();
		}
	}
}
