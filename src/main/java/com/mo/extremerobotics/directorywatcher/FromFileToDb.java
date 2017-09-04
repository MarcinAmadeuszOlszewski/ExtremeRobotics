package com.mo.extremerobotics.directorywatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.log4j.Logger;

import com.mo.extremerobotics.db.DbSave;
import com.mo.extremerobotics.download.Downloader;
import com.mo.extremerobotics.read.CsvReader;
import com.mo.extremerobotics.read.XmlReader;

public class FromFileToDb {
	final static Logger logger = Logger.getLogger(FromFileToDb.class);
	private XmlReader xmlReader = new XmlReader();
	private CsvReader csvReader = new CsvReader();
	private DbSave dbSave = new DbSave();

	public void file(Path pathToFile) {
		List<String[]> lines = null;
		
		//najprostsze rozwiazanie do zidentyfikowania pliku
		if (pathToFile.toString().endsWith(".xml")) {
			lines = xmlReader.readXml(pathToFile.toString());
		} else if (pathToFile.toString().endsWith(".csv")) {
			lines = csvReader.readCsv(pathToFile.toString());
		}else{
			logger.warn("file: unknown extension - skip file.");
		}

		boolean saveStatus = false;
		if (lines != null) {
			saveStatus = dbSave.saveList(lines);
		}
		
		if (saveStatus) {
			copyFileToArchive(pathToFile);
		} else {
			logger.warn("file: saveStatus - false: "+pathToFile);
		}
	}

	private void copyFileToArchive(Path pathToFile) {
		try {
			Downloader.verifyAndCreateStandardPaths();
			Files.move(pathToFile, Paths.get(Downloader.ARCH_DIR + pathToFile.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error("copyFileToArchive:", e);
		}
	}
}
