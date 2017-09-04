package com.mo.extremerobotics.read;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public class CsvReader {
	final static Logger logger = Logger.getLogger(CsvReader.class);
	private int[] positions = new int[12];// datePos, usdPos, eurPos, chfPos, uahPos, czkPos, hrkPos, phpPos, zarPos, rubPos, cnyPos, tab_noPos;

	public List<String[]> readCsv(String filename) {
		List<String[]> out = new ArrayList<>();
		try (BufferedReader in = Files.newBufferedReader(Paths.get(filename), StandardCharsets.ISO_8859_1);) {
			String line = "";
			boolean setHeaders = true;
			while ((line = in.readLine()) != null) {
				if (setHeaders) {
					setHeaders = false;
					setPos(line);
				}
				if (line.startsWith("20")) {
					out.add(setData(line));
				}
			}
		} catch (IOException e) {
			logger.error("readCsv: " + filename, e);
		}
		
		return out;
	}

	private void setPos(String line) {
		String[] headers = line.toUpperCase().split(";");
		for (int i = 0; i < headers.length; i++) {
			switch(headers[i]){
			case "DATA":positions[0] = i;break;
			case "1USD":positions[1] = i;break;
			case "1EUR":positions[2] = i;break;
			case "1CHF":positions[3] = i;break;
			case "1UAH":positions[4] = i;break;
			case "1CZK":positions[5] = i;break;
			case "1HRK":positions[6] = i;break;
			case "1PHP":positions[7] = i;break;
			case "1ZAR":positions[8] = i;break;
			case "1RUB":positions[9] = i;break;
			case "1CNY":positions[10] = i;break;
			case "NR TABELI":positions[11] = i;break;
			}
		}
	}

	private String[] setData(String line) {
		String[] out = new String[12];
		String[] data = line.replace(",", ".").split(";");
		for (int i = 0; i < data.length; i++) {
			for (int pos = 0; pos < 12; pos++) {
				if(positions[pos]==i){
					out[pos]=data[i];
					break;
				}
			}
		}
		return out;
	}
}
