package com.mo.extremerobotics.read;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlReader {
	final static Logger logger = Logger.getLogger(XmlReader.class);
	
	public List<String[]> readXml(String filename) {
		List<String[]> out = new ArrayList<>();
		File fXmlFile = new File(filename);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			String[] linia = new String[12];
			NodeList nList = doc.getElementsByTagName("data_publikacji");
			linia[0] = nList.item(0).getTextContent();

			nList = doc.getElementsByTagName("numer_tabeli");
			linia[11] = nList.item(0).getTextContent().split("/")[0];

			nList = doc.getElementsByTagName("pozycja");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				int pos = setPos(((Element) nNode).getElementsByTagName("kod_waluty").item(0).getTextContent());
				if (pos != -1)
					linia[pos] = ((Element) nNode).getElementsByTagName("kurs_sredni").item(0).getTextContent().replace(",", ".");
			}

			out.add(linia);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			logger.error("readXml: " + filename, e);
		}

		return out;
	}

	private int setPos(String code) {
		switch (code.toUpperCase()) {
		case "USD":
			return 1;
		case "EUR":
			return 2;
		case "CHF":
			return 3;
		case "UAH":
			return 4;
		case "CZK":
			return 5;
		case "HRK":
			return 6;
		case "PHP":
			return 7;
		case "ZAR":
			return 8;
		case "RUB":
			return 9;
		case "CNY":
			return 10;
		default:
			return -1;
		}
	}
}
