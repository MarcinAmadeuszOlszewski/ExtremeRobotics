/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mo.extremerobotics.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class Downloader {

	public static final String DEST_DIR = File.separator + "download" + File.separator;
	public static final String ARCH_DIR = File.separator + "download" + File.separator + "arch" + File.separator;

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
			e.printStackTrace();
		}
	}

	private URL connectToWebsite(String sourceUrl) {
		URL url = null;
		try {
			url = new URL(sourceUrl);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
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
			System.out.print(" +" + (destinationFile + filename));
		} catch (IOException e) {
			System.out.print(" -" + (destinationFile + filename) + " " + sourceUrl);
		}
		System.out.println();
	}

}
