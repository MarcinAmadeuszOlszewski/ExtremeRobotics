package com.mo.extremerobotics.db;

import java.util.Arrays;

import com.mo.extremerobotics.conf.DbConf;

public class Queries {

	private static String getMissingXml_MS = "SELECT year([date]) AS maxdat,tab_no FROM er_currency WHERE [date]=(SELECT max([date]) FROM er_currency)";
	private static String getMissingXml_PS = "SELECT EXTRACT(YEAR FROM date) AS maxdat,tab_no FROM er_currency WHERE date=(SELECT max(date) FROM er_currency);";
	private static String isTableFilled = "SELECT count(1) FROM er_currency";
	private static String saveLine_MS = "insert into [er_currency] ([date],[usd],[eur],[chf],[uah],[czk],[hrk],[php],[zar],[rub],[cny],[tab_no]) values (?,?,?,?,?,?,?,?,?,?,?,?);";
	private static String saveLine_PS = "insert into er_currency (date,usd,eur,chf,uah,czk,hrk,php,zar,rub,cny,tab_no) values (?,?,?,?,?,?,?,?,?,?,?,?);";

	public static String getMissingXml() {
		if (DbConf.curentDb.equals("PS"))
			return getMissingXml_PS;
		else
			return getMissingXml_MS;
	}

	public static String isTableFilled() {
		return isTableFilled;
	}

	public static String saveLine() {
		if (DbConf.curentDb.equals("PS"))
			return saveLine_PS;
		else
			return saveLine_MS;
	}

	public static String getData(String[] currencies) {
		if (DbConf.curentDb.equals("PS")) {
			return "SELECT date, " + Arrays.toString(currencies).replace("[", "").replace("]", "")
					+ " FROM er_currency WHERE date >= ? and date <=  ? order by date;";
		} else
			return "SELECT [date], " + Arrays.toString(currencies).replace("[", "").replace("]", "")
					+ " FROM er_currency WHERE [date] between ? and ? order by [date];";
	}
}
