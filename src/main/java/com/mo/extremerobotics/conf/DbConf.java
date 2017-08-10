package com.mo.extremerobotics.conf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConf {
	public static String curentDb = "PS";// "PS","MS"

	private DbConf() {
	}

	public static Connection getConnection() {
		Connection connection = null;
		String sqlDriverName = "";
		String databaseUrl = "";
		String databasePassword = "";
		String databaseUsername = "";
		if (DbConf.curentDb.equals("PS")) {
			// PostgreSQL
			sqlDriverName = "org.postgresql.Driver";
			databaseUrl = "jdbc:postgresql://127.0.0.1:5432/xxxx";
			databasePassword = "xxxx";
			databaseUsername = "xxxx";
		} else {
			// MSSQL
			sqlDriverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			databaseUrl = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=xxxx";
			databasePassword = "xxxx";
			databaseUsername = "xxxx";
		}

		try {
			Class.forName(sqlDriverName);
			connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
