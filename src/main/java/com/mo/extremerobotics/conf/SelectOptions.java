package com.mo.extremerobotics.conf;

import java.util.LinkedHashMap;
import java.util.Map;

public class SelectOptions {

	private SelectOptions() {
	}

	public static Map<String, String> createSelectOptions() {
		Map<String, String> selectOpt = new LinkedHashMap<>();
		selectOpt.put("USD", "Dolar amerykanski");
		selectOpt.put("EUR", "Euro");
		selectOpt.put("CHF", "Frank szwajcarski");
		selectOpt.put("UAH", "Hrywna");
		selectOpt.put("CZK", "Korona czeska");
		selectOpt.put("HRK", "Kuna");
		selectOpt.put("PHP", "Peso filipinskie");
		selectOpt.put("ZAR", "Rand");
		selectOpt.put("RUB", "Rubel rosyjski");
		selectOpt.put("CNY", "Yuan renminbi");
		return selectOpt;
	}
}
