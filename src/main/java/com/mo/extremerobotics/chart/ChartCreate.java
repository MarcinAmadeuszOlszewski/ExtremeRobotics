package com.mo.extremerobotics.chart;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.mo.extremerobotics.db.DbRead;
import com.mo.extremerobotics.trendlinelib.PolyTrendLine;
import com.mo.extremerobotics.trendlinelib.TrendLine;

public class ChartCreate {
	private String[] colors = new String[] {"#003366", "#ff3399", "#9966ff", "#00ff99", "#003300", 
			"#000066", "#660066", "#993333", "#663300", "#99ff33" };

	public String createChart(String dateFrom, String dateTo, String[] currencies, int future, String trend) {
		DbRead dbRead = new DbRead();
		ArrayList<ArrayList<String>> dataTmp = dbRead.getData(dateFrom, dateTo, currencies);
		TrendLine[] trends = calculateTrendLine(dataTmp);
		
		StringBuilder out = new StringBuilder(
				"var chart = AmCharts.makeChart(\"chartdiv\",{\"type\" : \"serial\",\"theme\" : \"light\",\"marginRight\" : 80,\"autoMarginOffset\" : 20,\"dataDateFormat\" : \"YYYY-MM-DD HH\",");


		// dane
		out.append(dataProvider(currencies, dataTmp,trends, future));

		// opisy danych
		out.append(graphs(currencies));

		// linie trendu t.predict(12)
		if(trend!=null)
		out.append(trendLines(trends, dataTmp ));

		// reszta
		out.append("\"legend\": {\"enabled\": true,\"useGraphSettings\": true},");
		out.append("\"valueAxes\" : [ {\"axisAlpha\" : 0,\"guides\" : [ {\"fillAlpha\" : 0.1,\"fillColor\" : \"#888888\",\"lineAlpha\" : 0,\"toValue\" : 16,\"value\" : 10} ],\"position\" : \"left\",\"tickLength\" : 0} ],");
		out.append("\"chartScrollbar\" : {\"scrollbarHeight\" : 2,\"offset\" : -1,\"backgroundAlpha\" : 0.1,\"backgroundColor\" : \"#888888\",\"selectedBackgroundColor\" : \"#67b7dc\",\"selectedBackgroundAlpha\" : 1},");
		out.append("\"chartCursor\" : {\"fullWidth\" : true,\"valueLineEabled\" : true,\"valueLineBalloonEnabled\" : true,\"valueLineAlpha\" : 0.5,\"cursorAlpha\" : 0},\"categoryField\" : \"date\",");
		out.append("\"categoryAxis\" : {\"parseDates\" : true,\"axisAlpha\" : 0,\"gridAlpha\" : 0.1,\"minorGridAlpha\" : 0.1,\"minorGridEnabled\" : true},\"mouseWheelZoomEnabled\": true,\"export\" : {\"enabled\" : true}});");

		return out.toString();
	}

	private StringBuilder trendLines(TrendLine[] trends, ArrayList<ArrayList<String>> dataTmp) {
		int dataLength=dataTmp.size()-1;
		StringBuilder sb = new StringBuilder("\"trendLines\" : [ ");
		for (int i = 0; i < trends.length; i++) {
		sb.append("{\"finalDate\" : \"").append(dataTmp.get(dataLength).get(0)).append(" 12\",\"finalValue\" : ").append(trends[i].predict(dataLength));
		sb.append(",\"initialDate\" : \"").append(dataTmp.get(0).get(0)).append(" 12\",\"initialValue\" : ").append(trends[i].predict(0)).append(",\"lineAlpha\" : 0.5,\"lineColor\" : \"").append(colors[i]).append("\"}");
		if (i < trends.length - 1)
			sb.append(",");
	}
		sb.append("],");
		return sb;
	}

	private StringBuilder graphs(String[] currencies) {
		StringBuilder sb = new StringBuilder("\"graphs\" : [");
		for (int i = 0; i < currencies.length; i++) {
			sb.append("{\"balloonText\" : \"[[category]]<br><b><span style='font-size:10px;'>").append(currencies[i]).append(":[[value]]</span></b>\",\"colorField\" : \"color\",\"valueField\" : \"");
			sb.append(currencies[i]).append("\",\"lineColor\" : \"").append(colors[i]).append("\",  \"title\": \"").append(currencies[i]).append("\", \"dashLengthField\": \"dashLengthLine\"}");
			if (i < currencies.length - 1)
				sb.append(",");
				}
		sb.append("],");
		return sb;
	}

	private StringBuilder dataProvider(String[] currencies, ArrayList<ArrayList<String>> dataTmp,TrendLine[] trends, int future) {
		StringBuilder sb = new StringBuilder("\"dataProvider\" : [ ");
		for (int days = 0; days < dataTmp.size(); days++) {
			ArrayList<String> ar = dataTmp.get(days);
			sb.append("{\"date\" : \"").append(ar.get(0)).append("\",");
			for (int i = 1; i < ar.size(); i++) {
				sb.append("\"").append(currencies[i - 1]).append("\" : ").append(ar.get(i));
				if (i < ar.size() - 1)
					sb.append(", ");
			}

			if (days < dataTmp.size() - 1)
				sb.append("},");
			else
				sb.append("}");
		}
		
		//prognozowanie
		sb.append(forecasting(currencies, dataTmp, trends, future));
		
		sb.append("],");
		return sb;
	}

	private StringBuilder forecasting(String[] currencies, ArrayList<ArrayList<String>> dataTmp, TrendLine[] trends, int future) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate lastChoosenDay = LocalDate.parse(dataTmp.get(dataTmp.size()-1).get(0), formatter);
		int dataLength=dataTmp.size()-1;
		double[] delta=new double[trends.length];
		for(int i =0;i<trends.length;i++){
			delta[i]=Double.parseDouble(dataTmp.get(dataLength).get(i+1))-trends[i].predict(dataLength);
		}
		StringBuilder sb=new StringBuilder();
		for (int days = 0; days <= future; days++) {
			sb.append(",");
			sb.append("{\"dashLengthLine\": 5,\"date\" : \"").append(lastChoosenDay.plusDays(days)).append("\", ");
			for (int i = 1; i < dataTmp.get(0).size(); i++) {
				double value=delta[i-1]+trends[i-1].predict(dataLength+days);
				sb.append("\"").append(currencies[i - 1]).append("\" : ").append( String.format("%.4f", value).replace(",", "."));	//bez zaokraglania - bez sensu dla "wrozenia"
				if (i < dataTmp.get(0).size() - 1)
					sb.append(", ");
			}
			sb.append("}");
		}
		return sb;
	}
	
	private TrendLine[] calculateTrendLine(ArrayList<ArrayList<String>> dataTmp) {
			//tabele pomocnicze
			double[] y = new double[dataTmp.size()];
			double[] x = new double[dataTmp.size()];
			for (int i = 0; i < dataTmp.size(); i++) {
				x[i] = i;
			}
			//wynikowa tabela
			TrendLine[] out = new PolyTrendLine[dataTmp.get(0).size()-1];
			for (int i = 0; i < dataTmp.get(0).size()-1; i++) {
				out[i] = new PolyTrendLine(2);
			}
		
			//zbieranie danych - zawasze musi byc wybrana przynajmniej jedna waluta
			for (int j = 1; j < dataTmp.get(0).size(); j++) {
				for (int i = 0; i < dataTmp.size(); i++) {
					y[i] = Double.parseDouble(dataTmp.get(i).get(j));
				}
				out[j - 1].setValues(y, x);
			}

		return out;
	}
	
}
