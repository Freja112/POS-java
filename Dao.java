package swing_design;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dao {
	private static Dao singleton;
	private Map<String, String> sqlMap;
	private Connection conn;
	private Statement stmt = null;
	private PreparedStatement preparedStatement = null;
	
	public static Dao getinstance() {		// �̱��� ����� ���� ��𼭳� �� Ŭ������ �ս��� ����� �� �ֵ��� ��
		
		if(singleton == null) {
			singleton = new Dao();
		}
		
		return singleton;
		
	}

	public Dao() {				// ������, ������ ���� �ؽøʰ��� ����
		sqlMap = new HashMap<String, String>();
		sqlMap.put("select_object_list", "SELECT * FROM object_list");
		sqlMap.put("select_object_serial", "SELECT * FROM object_list WHERE serial = ?");
		sqlMap.put("select_object_name", "SELECT * FROM object_list WHERE name = ?");
		sqlMap.put("select_object_company", "SELECT * FROM object_list WHERE company = ?");
		sqlMap.put("delete_object", "DELETE FROM object_list WHERE serial = ?");
		sqlMap.put("update_object", "UPDATE object_list SET name = ?, company = ?, price = ?, serial = ? WHERE serial = ?");
		sqlMap.put("insert_object", "INSERT INTO object_list VALUES (?, ?, ?, ?)");
		
		sqlMap.put("select_sell_list", "SELECT * FROM sell_list");
		sqlMap.put("select_sell_name", "SELECT * FROM sell_list WHERE name = ?");
		sqlMap.put("select_sell_company", "SELECT * FROM sell_list WHERE company = ?");
		sqlMap.put("select_sell_serial", "SELECT * FROM sell_list WHERE serial = ?");
		sqlMap.put("delete_sell", "DELETE FROM sell_list WHERE serial = ?");
		sqlMap.put("insert_sell", "INSERT INTO sell_list VALUES (?, ?, ?, ?, ?)");
	}
	
	public String getQuery(String name) {	// ���ڷ� ���� sqlMap�� Ű�� �ش��ϴ� ���� ��ȯ����
		if(sqlMap.get(name) == null) {
			return "not query";
		}		
		return sqlMap.get(name);		
	}
	
			
	private void connectionDB() {	// DB�� ����
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://URL:3306/DB_NAME", "ID", "PW");
				stmt = conn.createStatement();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
	}
	
	public List<Map<String, String>> executeDBList(String sqlname, List<String> params) {	// SELECT���� ������ �� �������� �۾��� �ش��ϴ� �κ��� ���, ��ü���� �����κа� �Ķ���Ͱ��� ���ڷ� �޴´�.
		try {
			connectionDB();
			
			preparedStatement = conn.prepareStatement(getQuery(sqlname));
			
			if(params != null) {		// ���ڷ� �Ķ���Ͱ� �Ѿ���� �� ��� �Ķ���͸� PreparedStatement�� ? �κп� ������� �־���
				int i = 1;
				for (String item : params) {
					preparedStatement.setString(i++, item);
				}
			}
			
			ResultSet rs = preparedStatement.executeQuery();	// �ϼ��� ������ ���� ����� ResultSet���� ����
			
			ResultSetMetaData rsmd = rs.getMetaData();		// ResultSet�� ���� ��Ÿ�����͸� ����
			int cCount = rsmd.getColumnCount();
			
			
			List<Map<String, String>> resultData = new ArrayList<Map<String,String>>();
			Map<String, String> rowData;
			while(rs.next()) {			// ResultSet�� �ִ� ������ �ؽø��� ������ ������ List�� ��������
				rowData = new HashMap<String, String>();

				for (int j = 1; j < cCount + 1; j++ ) {
				  rowData.put(rsmd.getColumnName(j), rs.getString(j));
				}
				
				resultData.add(rowData);
			}
			
			conn.close();
			
			return resultData;		// ResultSet���� ���� ������ ��������� List< Map<String, String> >���·� ��ȯ�Ͽ� ��ȯ
			
		} catch (SQLException e) {
			System.out.println("GET DATA ERROR");			
			return null;
		}
	}
	
	public void executeDBUpdate(String sqlname, List<String> params) {		// INSERT, UPDATE, DELETE ���� ������ ������, ���� ���� ����
		try {
			connectionDB();
			
			preparedStatement = conn.prepareStatement(getQuery(sqlname));
			if(params != null) {
				int i = 1;
				for (String item : params) {
					preparedStatement.setString(i++, item);
				}
			}
			preparedStatement.executeUpdate();
			
			conn.close();
			
		} catch (SQLException e) {
			System.out.println("GET DATA ERROR");
		}
	}
}
