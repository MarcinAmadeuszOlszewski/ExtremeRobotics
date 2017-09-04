package com.mo.extremerobotics.download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mo.extremerobotics.conf.Params;
import com.mo.extremerobotics.db.DbRead;
import com.mo.extremerobotics.read.CsvReader;

public class DownloaderXml extends Downloader {
	public static final String AKT_DZIEN = "http://www.nbp.pl/kursy/xml/";

	private List<String> getMissingXml(Map<Params, String> params) {
		List<String> out = new ArrayList<>();
		URL url = null;
		try {
			url = new URL("http://www.nbp.pl/kursy/xml/dir.txt");
		} catch (MalformedURLException e) {
			logger.error("getMissingXml: ", e);
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));) {
			boolean lastFinded = false;
			String line = "";
			while ((line = in.readLine()) != null) {
				if (!line.startsWith("a"))
					continue;

				if (lastFinded) {
					out.add(line + ".xml");
				}
				if (!lastFinded && line.contains(params.get(Params.maxTableNo))
						&& line.contains(params.get(Params.maxDate))) {
					lastFinded = true;
				}
			}
		} catch (IOException e) {
			logger.error("getMissingXml: ", e);
		}
		return out;
	}

	public void dowloadActualCurrency() {
		Map<Params, String> params = DbRead.getLastTableNo();
		List<String> out = getMissingXml(params);
		for (String s : out) {
			saveFile(AKT_DZIEN + s, DEST_DIR);
		}
	}
}
