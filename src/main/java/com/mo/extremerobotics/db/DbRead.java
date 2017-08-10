package com.mo.extremerobotics.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mo.extremerobotics.conf.DbConf;
import com.mo.extremerobotics.conf.Params;

public class DbRead {

	public ArrayList<ArrayList<String>> getData(String dateFrom, String dateTo, String[] currencies) {
		ArrayList<ArrayList<String>> out = new ArrayList<>();

		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Connection connection = DbConf.getConnection();

			PreparedStatement stmt = connection.prepareStatement(Queries.getData(currencies));
			stmt.setDate(1, new java.sql.Date(format.parse(dateFrom).getTime()));
			stmt.setDate(2, new java.sql.Date(format.parse(dateTo).getTime()));
			ResultSet rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ArrayList<String> tmp = new ArrayList<>();
					tmp.add(rs.getString(1).substring(0, 10));
					for (int i = 2; i <= currencies.length + 1; i++) {
						tmp.add(rs.getString(i));
					}
					out.add(tmp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;
	}

	public static Map<Params, String> getLastTableNo() {
		Connection connection = DbConf.getConnection();
		Map<Params, String> params = new HashMap<>();
		try (Statement s1 = connection.createStatement();
				ResultSet rs = s1.executeQuery(Queries.getMissingXml());) {

			if (rs != null) {
				while (rs.next()) {
					params.put(Params.maxDate, rs.getString("maxdat").substring(2));
					params.put(Params.maxTableNo, rs.getString("tab_no"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConf.closeConnection(connection);
		}
		return params;
	}
}
