package com.mo.extremerobotics.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.mo.extremerobotics.conf.DbConf;

public class DbSave {
	private DateFormat formatCsv = new SimpleDateFormat("yyyyMMdd");
	private DateFormat formatXml = new SimpleDateFormat("yyyy-MM-dd");

	public boolean saveList(List<String[]> list) {
		Connection connection = DbConf.getConnection();
		boolean out = true;
		for (String[] line : list) {
			out = saveLine(line, connection);
			if (!out)
				break;
		}
		DbConf.closeConnection(connection);
		return out;
	}

	private boolean saveLine(String[] line, Connection connection) {
		boolean out = false;
		if (line.length != 12)
			return out;
		try {
			PreparedStatement stmt = connection.prepareStatement(Queries.saveLine());
			DateFormat format = line[0].length() == 8 ? formatCsv : formatXml;
			stmt.setDate(1, new java.sql.Date(format.parse(line[0]).getTime()));
			for (int i = 1; i < 11; i++){
				stmt.setBigDecimal(i + 1, new BigDecimal(line[i]));
			}
			
			stmt.setInt(12, Integer.parseInt(line[11]));

			int i = stmt.executeUpdate();
			out = i == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;
	}
}
