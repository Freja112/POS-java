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
	
	public static Dao getinstance() {		// 싱글톤 기법을 통해 어디서나 이 클래스를 손쉽게 사용할 수 있도록 함
		
		if(singleton == null) {
			singleton = new Dao();
		}
		
		return singleton;
		
	}

	public Dao() {				// 생성자, 쿼리에 대한 해시맵값을 설정
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
	
	public String getQuery(String name) {	// 인자로 받은 sqlMap의 키에 해당하는 값을 반환해줌
		if(sqlMap.get(name) == null) {
			return "not query";
		}		
		return sqlMap.get(name);		
	}
	
			
	private void connectionDB() {	// DB에 연결
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
	
	public List<Map<String, String>> executeDBList(String sqlname, List<String> params) {	// SELECT문을 수행할 때 공통적인 작업에 해당하는 부분을 담당, 전체적인 쿼리부분과 파라미터값을 인자로 받는다.
		try {
			connectionDB();
			
			preparedStatement = conn.prepareStatement(getQuery(sqlname));
			
			if(params != null) {		// 인자로 파라미터가 넘어왔을 때 모든 파라미터를 PreparedStatement의 ? 부분에 순서대로 넣어줌
				int i = 1;
				for (String item : params) {
					preparedStatement.setString(i++, item);
				}
			}
			
			ResultSet rs = preparedStatement.executeQuery();	// 완성된 쿼리를 날려 결과를 ResultSet으로 받음
			
			ResultSetMetaData rsmd = rs.getMetaData();		// ResultSet에 대한 메타데이터를 얻음
			int cCount = rsmd.getColumnCount();
			
			
			List<Map<String, String>> resultData = new ArrayList<Map<String,String>>();
			Map<String, String> rowData;
			while(rs.next()) {			// ResultSet에 있는 값들을 해시맵을 값으로 가지는 List에 대입해줌
				rowData = new HashMap<String, String>();

				for (int j = 1; j < cCount + 1; j++ ) {
				  rowData.put(rsmd.getColumnName(j), rs.getString(j));
				}
				
				resultData.add(rowData);
			}
			
			conn.close();
			
			return resultData;		// ResultSet으로 받은 값들을 결과적으로 List< Map<String, String> >형태로 변환하여 반환
			
		} catch (SQLException e) {
			System.out.println("GET DATA ERROR");			
			return null;
		}
	}
	
	public void executeDBUpdate(String sqlname, List<String> params) {		// INSERT, UPDATE, DELETE 문을 수행을 도와줌, 위와 같은 원리
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
