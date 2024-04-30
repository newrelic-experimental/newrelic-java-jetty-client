package com.newrelic.instrumentation.labs.jetty.client;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpFields;

public class Utils {
	public static Map<String, String> convertToMap(HttpFields httpFields) {
		Map<String, String> map = new HashMap<>();
		for (HttpField httpField : httpFields) {
			map.put(httpField.getName(), httpField.getValue());
		}
		return map;
	}
}
