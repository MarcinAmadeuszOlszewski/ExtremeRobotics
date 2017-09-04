package com.mo.extremerobotics.db;

import java.util.Arrays;

import com.mo.extremerobotics.conf.DbConf;

public class Queries {

	private static String getMissingXml = "SELECT EXTRACT(YEAR FROM date) AS maxdat,tab_no FROM er_currency WHERE date=(SELECT max(date) FROM er_currency);";
	private static String isTableFilled = "SELECT count(1) FROM er_currency";
	private static String saveLine = "insert into er_currency (date,usd,eur,chf,uah,czk,hrk,php,zar,rub,cny,tab_no) values (?,?,?,?,?,?,?,?,?,?,?,?);";

	public static String getMissingXml() {
			return getMissingXml;
	}

	public static String isTableFilled() {
		return isTableFilled;
	}

	public static String saveLine() {
			return saveLine;
	}

	public static String getData(String[] currencies) {
			return "SELECT date, " + Arrays.toString(currencies).replace("[", "").replace("]", "")
					+ " FROM er_currency WHERE date >= ? and date <=  ? order by date;";
	}
}
