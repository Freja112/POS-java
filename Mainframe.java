package swing_design;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;


public class Mainframe extends JPanel implements TableModelListener, ActionListener {

	JTextArea text_object;
	JButton button_buy, button_rm, button_list, button_quit, button_input, button_sell_list;
	JTextField input_sn, price_sum;
	JTable table;
	ObjectModel oModel;
	JScrollPane scrollPane;
	JLabel sumLabel;
	ListFrame listDialog;
	SellListFrame sellListDialog;
	Box box, emptybox;

	GridBagConstraints c;

	public Mainframe() {
		c = new GridBagConstraints();
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;

		this.setLayout(new GridBagLayout());

		
		
		
		/*콤포넌트 초기화*/
		text_object = new JTextArea(20, 60);
		input_sn = new JTextField();
		input_sn.addActionListener(this);
		price_sum = new JTextField();
		sumLabel = new JLabel("총 금액 : ");
		box = Box.createHorizontalBox();
		emptybox = Box.createHorizontalBox();
		oModel = new ObjectModel();
		table = new JTable(oModel);
		button_buy = new JButton("구매 확정");
		button_rm = new JButton("선택 삭제");
		button_list = new JButton("상품 목록");
		button_quit = new JButton("종료");
		button_input = new JButton("입력");
		button_sell_list = new JButton("거래 내역");

		
		
		
		/*콤포넌트 설정*/
		button_buy.addActionListener(this);
		button_rm.addActionListener(this);
		button_list.addActionListener(this);
		button_quit.addActionListener(this);
		button_input.addActionListener(this);
		button_sell_list.addActionListener(this);
		table.getModel().addTableModelListener(this);
		table.getTableHeader().setReorderingAllowed(false);
		sumLabel.setHorizontalAlignment(JLabel.RIGHT);
		price_sum.setHorizontalAlignment(JTextField.RIGHT);		
		price_sum.enable(false);

		
		
		
		/*콤포넌트 배치*/
		box.add(table);
		box.add(new JScrollPane(table));
		c.weightx = 2;
		c.weighty = 2;
		layout(box, 0, 0, 5, 4);
		c.weightx = 1;
		layout(button_buy, 5, 0, 1, 1);
		layout(button_rm, 5, 1, 1, 1);
		layout(button_list, 5, 2, 1, 1);
		layout(button_sell_list, 5, 3, 1, 1);
		c.weighty = 0;
		c.weightx = 1;
		layout(input_sn, 0, 6, 4, 1);
		c.weightx = 0;
		c.weighty = 1;
		layout(button_quit, 5, 4, 1, 3);
		layout(emptybox, 0, 5, 1, 1);
		c.weighty = 0;
		layout(price_sum, 1, 4, 4, 1);
		layout(sumLabel, 0, 4, 1, 1);
		layout(button_input, 4, 6, 1, 1);
	}

	public void layout(Component obj, int x, int y, int width, int height) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		add(obj, c);
	}

	public static void main(String[] args) {
		Connection conn;
		Statement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://URL:3306/DB_NAME", "ID", "PW");
			stmt = conn.createStatement();

			JFrame frame = new JFrame("Pos Program");
			Mainframe panel = new Mainframe();

			frame.getContentPane().add(panel);
			frame.pack();
			frame.setSize(1024, 768);
			frame.setVisible(true);
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC 드라이버 로드 에러");
		} catch (SQLException e) {
			System.out.println("DB연결 오류");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == button_input || source == input_sn) { // 입력 버튼 리스너

			List<String> params = new ArrayList<String>();
			params.add(input_sn.getText());

			List<Map<String, String>> getData = Dao.getinstance().executeDBList("select_object_serial", params);

			List<String> setData = new ArrayList<String>();

			for (Map item : getData) {
				setData.add(item.get("name").toString());
				setData.add(item.get("company").toString());
				setData.add(item.get("price").toString());
				setData.add(item.get("serial").toString());
			}

			oModel.setValueRow(setData);
			input_sn.setText("");
		} else if (source == button_buy) { // 구매 확정 버튼 리스너
			int row = oModel.getRowCount();
			List<String> paramsIns = new ArrayList<String>();
			List<String> paramsDel = new ArrayList<String>();
			
			java.util.Date dt = new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			
			for(int i=0; i<row; i++){
				paramsIns.clear();
				paramsDel.clear();
				paramsIns.add((String)oModel.getValueAt(i, 0));
				paramsIns.add((String)oModel.getValueAt(i, 1));
				paramsIns.add((String)oModel.getValueAt(i, 2));
				paramsIns.add((String)oModel.getValueAt(i, 3));
				paramsIns.add(currentTime);
				paramsDel.add((String)oModel.getValueAt(i, 3));

				Dao.getinstance().executeDBUpdate("insert_sell", paramsIns);
				Dao.getinstance().executeDBUpdate("delete_object", paramsDel);
			}
			
			oModel.clearTable();
		} else if (source == button_rm) { // 선택 삭제 버튼 리스너
			oModel.removeAtIndexRow(table.getSelectedRow());
		} else if (source == button_list) { // 상품 목록 버튼 리스너
			listDialog = new ListFrame();
		} else if (source == button_quit) { // 종료 버튼 리스너
			System.exit(0);
		} else if (source == button_sell_list) {	/// 거래 내역 버튼 리스너
			sellListDialog = new SellListFrame();
		}
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
		Integer sum=0;
		int rowCount = oModel.getRowCount();
		for(int i=0; i<rowCount; i++){
			sum += Integer.parseInt((String)oModel.getValueAt(i, 2));
		}
		price_sum.setText(sum.toString());
	}
}
