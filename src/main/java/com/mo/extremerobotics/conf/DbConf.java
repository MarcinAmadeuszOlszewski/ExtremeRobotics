package com.mo.extremerobotics.conf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import static com.mo.extremerobotics.Start.PROPERTIES;

public class DbConf {
	final static Logger logger = Logger.getLogger(DbConf.class);

	private DbConf() {
	}

	public static Connection getConnection() {
		Connection connection = null;
		String sqlDriverName = PROPERTIES.getProperty("sqlDriverName");
		String databaseUrl = PROPERTIES.getProperty("databaseUrl");
		String databasePassword = PROPERTIES.getProperty("databasePassword");
		String databaseUsername = PROPERTIES.getProperty("databaseUsername");

		try {
			Class.forName(sqlDriverName);
			connection = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
		} catch (SQLException | ClassNotFoundException e) {
			logger.error("getConnection:", e);
		}
		return connection;
	}

	public static void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("closeConnection:", e);
			}
		}
	}

}
