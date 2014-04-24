package com.zxq.rts.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.zxq.rts.rabbit.error.ServiceException;
import com.zxq.rts.utils.PropUtil;

public class MysqlConn {

	private static Statement stmt;

	static {
		try {
			Class.forName(PropUtil.getPropVal("driver").toString());
			Connection conn = DriverManager.getConnection(PropUtil.getPropVal("mysqlurl").toString(), PropUtil.getPropVal("mysqlusername").toString(), PropUtil.getPropVal("mysqlpassword").toString());
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServiceException se) {

		}
	}

	public static void insert(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ResultSet select(String sql) {
		try {
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int update(String sql) {
		try {
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
