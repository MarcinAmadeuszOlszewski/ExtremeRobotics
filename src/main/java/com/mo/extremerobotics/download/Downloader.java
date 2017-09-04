package com.mo.extremerobotics.download;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import com.mo.extremerobotics.read.CsvReader;

import static com.mo.extremerobotics.Start.PROPERTIES;
import static java.io.File.separator;

public abstract class Downloader {
	final static Logger logger = Logger.getLogger(Downloader.class);
	public static final String DEST_DIR = separator + PROPERTIES.getProperty("homeDir") + separator + "download"
			+ separator;
	public static final String ARCH_DIR = separator + PROPERTIES.getProperty("homeDir") + separator + "download"
			+ separator + "arch" + separator;

	public void saveFile(String sourceUrl, String destinationFile) {
		verifyAndCreateStandardPaths();

		URL url = connectToWebsite(sourceUrl);

		downloadFile(sourceUrl, destinationFile, url);
	}

	public static void verifyAndCreateStandardPaths() {
		try {
			if (!Files.isDirectory(Paths.get(Downloader.DEST_DIR))) {
				Files.createDirectory(Paths.get(Downloader.DEST_DIR));
			}
			if (!Files.isDirectory(Paths.get(Downloader.ARCH_DIR))) {
				Files.createDirectory(Paths.get(Downloader.ARCH_DIR));
			}
		} catch (IOException e) {
			logger.error("verifyAndCreateStandardPaths: ", e);
		}
	}

	private URL connectToWebsite(String sourceUrl) {
		URL url = null;
		try {
			url = new URL(sourceUrl);
		} catch (MalformedURLException e) {
			logger.error("connectToWebsite: ", e);
		}
		return url;
	}

	private void downloadFile(String sourceUrl, String destinationFile, URL url) {
		String[] filenameTab = sourceUrl.split("/");
		String filename = filenameTab[filenameTab.length - 1];
		try (InputStream is = url.openStream(); OutputStream os = new FileOutputStream(destinationFile + filename);) {

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			logger.info(" +" + (destinationFile + filename));
		} catch (IOException e) {
			logger.info(" -" + (destinationFile + filename) + " " + sourceUrl);
			logger.error("downloadFile: ", e);
		}
	}

}
