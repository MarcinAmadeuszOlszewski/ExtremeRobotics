package com.mo.extremerobotics.read;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
	private int datePos, usdPos, eurPos, chfPos, uahPos, czkPos, hrkPos, phpPos, zarPos, rubPos, cnyPos, tab_noPos;
	
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
			System.err.println("readCsv " + filename);
			e.printStackTrace();
		}
		return out;
	}

	private void setPos(String line) {
		String[] headers = line.split(";");
		for (int i = 0; i < headers.length; i++) {
			if ("data".equalsIgnoreCase(headers[i]))
				datePos = i;
			else if ("1USD".equalsIgnoreCase(headers[i]))
				usdPos = i;
			else if ("1EUR".equalsIgnoreCase(headers[i]))
				eurPos = i;
			else if ("1CHF".equalsIgnoreCase(headers[i]))
				chfPos = i;
			else if ("1UAH".equalsIgnoreCase(headers[i]))
				uahPos = i;
			else if ("1CZK".equalsIgnoreCase(headers[i]))
				czkPos = i;
			else if ("1HRK".equalsIgnoreCase(headers[i]))
				hrkPos = i;
			else if ("1PHP".equalsIgnoreCase(headers[i]))
				phpPos = i;
			else if ("1ZAR".equalsIgnoreCase(headers[i]))
				zarPos = i;
			else if ("1RUB".equalsIgnoreCase(headers[i]))
				rubPos = i;
			else if ("1CNY".equalsIgnoreCase(headers[i]))
				cnyPos = i;
			else if ("nr tabeli".equalsIgnoreCase(headers[i]))
				tab_noPos = i;
		}
	}

	private String[] setData(String line) {
		String[] out = new String[12];
		String[] data = line.replace(",", ".").split(";");
		for (int i = 0; i < data.length; i++) {
			if (datePos == i)
				out[0] = data[i];
			else if (usdPos == i)
				out[1] = data[i];
			else if (eurPos == i)
				out[2] = data[i];
			else if (chfPos == i)
				out[3] = data[i];
			else if (uahPos == i)
				out[4] = data[i];
			else if (czkPos == i )
				out[5] = data[i];
			else if (hrkPos == i )
				out[6] = data[i];
			else if (phpPos == i )
				out[7] = data[i];
			else if (zarPos == i )
				out[8] = data[i];
			else if (rubPos == i )
				out[9] = data[i];
			else if (cnyPos == i )
				out[10] = data[i];
			else if (tab_noPos == i)
				out[11] = data[i];
		}
		return out;
	}

}
