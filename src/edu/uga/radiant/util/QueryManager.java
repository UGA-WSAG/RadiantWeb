package edu.uga.radiant.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryManager {

	private Connection conn;
	
	private long userID;
	
	private String accountType;
	
	public QueryManager(Connection conn, long userID, String accountType){
		this.conn = conn;
		this.userID = userID;
		this.accountType = accountType;
	}
	
	public static long getWSDLID(Connection conn, String name, String type) throws SQLException{
		
		long result = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "Select id from wsdl where name = ? and type = ? ;";
		
		pstmt = conn.prepareStatement(SQL);
		pstmt.setString(1, name);
		pstmt.setString(2, type);
		rs = pstmt.executeQuery();
		
		// put result into classOBJs
		while (rs.next()) {
			result = rs.getLong(1);
		}
		return result;
		
	}
	
	public static String getMD5(Connection conn, long id) throws SQLException{
		
		String result = "";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "Select md5 from wsdl where id = ? ;";
		
		pstmt = conn.prepareStatement(SQL);
		pstmt.setLong(1, id);
		rs = pstmt.executeQuery();
		
		// put result into classOBJs
		while (rs.next()) {
			result = rs.getString(1);
		}
		
		return result;
	}

	public static boolean checkValidate(Connection conn, long id) throws SQLException {
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "Select validate from wsdl where id = ? ;";
		
		pstmt = conn.prepareStatement(SQL);
		pstmt.setLong(1, id);
		rs = pstmt.executeQuery();
		
		// put result into classOBJs
		while (rs.next()) {
			result = rs.getBoolean(1);
		}
		return result;
	}

	public static String getMD5(Connection conn, String name, String type) throws SQLException {

		String result = "";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "Select md5 from wsdl where name = ? and type = ? ;";
		
		pstmt = conn.prepareStatement(SQL);
		pstmt.setString(1, name);
		pstmt.setString(2, type);
		rs = pstmt.executeQuery();
		
		// put result into classOBJs
		while (rs.next()) {
			result = rs.getString(1);
		}
		
		return result;
	}


	public static String getWSDL(Connection conn, String name, String type) throws SQLException {
		
		String result = "";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "Select xml from wsdl where name = ? and type = ? ;";
		
		pstmt = conn.prepareStatement(SQL);
		pstmt.setString(1, name);
		pstmt.setString(2, type);
		rs = pstmt.executeQuery();
		
		// put result into classOBJs
		while (rs.next()) {
			result = rs.getString(1);
		}
		return result;
		
	}


	public static ArrayList<String> getSAWSDLList(Connection connection) throws SQLException {
		
		ArrayList<String> list = new ArrayList<String>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "Select name from wsdl where type = 'sawsdl' and validate = ? ;";
		
		pstmt = connection.prepareStatement(SQL);
		pstmt.setBoolean(1, true);
		rs = pstmt.executeQuery();
		
		// put result into classOBJs
		while (rs.next()) {
			list.add(rs.getString(1));
		}
		
		return list;
		
	}


	public static ArrayList<String> getWSDLList(Connection connection) throws SQLException {
		
		ArrayList<String> list = new ArrayList<String>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "Select name from wsdl where type = 'wsdl' and validate = ? ;";
		
		pstmt = connection.prepareStatement(SQL);
		pstmt.setBoolean(1, true);
		rs = pstmt.executeQuery();
		
		// put result into classOBJs
		while (rs.next()) {
			list.add(rs.getString(1));
		}
		
		return list;
	}


	public static ArrayList<String> search(Connection conn, String searchWord) throws URISyntaxException, IOException, SQLException {

		String values = DataManager.tokenize(searchWord);
		
		ArrayList<String> result = new ArrayList<String>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "Select W.name, W.type, COUNT(*) as num " +
						"from keyword K, wsdl W " +
						"where " +
							"K.keyword IN (" + values + ") and K.id = W.id " +
						"group by K.id " +
						"order by num desc " +
						"limit 50 ; ";
		
		pstmt = conn.prepareStatement(SQL);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			result.add(rs.getString(1) + "." + rs.getString(2));
		}
		
		return result;
	
	}
	
	
	
}