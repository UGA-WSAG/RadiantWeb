package edu.uga.radiant.util;

import java.sql.*;

public class DataBaseConnection {

	private final String DBDRIVER = "com.mysql.jdbc.Driver";
	private static String DBURL;
	private static String DB;
	private static String DBUSER;
	private static String DBPASSWORD;
	private Connection conn = null;

	public DataBaseConnection() {
		
		try {
			RadiantToolConfig config = new RadiantToolConfig();
			if (DB == null) DB = config.getDbName();
			if (DBUSER == null) DBUSER = config.getDbUserId();
			if (DBPASSWORD == null) DBPASSWORD = config.getDbPassword();
			if (DBURL == null) DBURL = "jdbc:mysql://localhost/" + DB + "?useUnicode=true&characterEncoding=utf-8";
			Class.forName(DBDRIVER);
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		try {
			Class.forName(DBDRIVER);
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return this.conn;
	}

	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
	
