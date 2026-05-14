package com.newrelic.instrumentation.labs.jetty.client;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.client.Request;
import org.eclipse.jetty.client.Response;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpFields;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.TransactionNamePriority;

public class Utils {

	public static Map<String, String> convertToMap(HttpFields httpFields) {
		Map<String, String> map = new HashMap<>();
		for (HttpField httpField : httpFields) {
			map.put(httpField.getName(), httpField.getValue());
		}
		return map;
	}

	public static void addAttribute(Map<String, Object> attributes, String key, Object value) {
		if (attributes != null && key != null && !key.isEmpty() && value != null) {
			attributes.put(key, value);
		}
	}

	public static void addRequest(Map<String, Object> attributes, Request request) {
		if (request != null) {
			addAttribute(attributes, "Request-Method", request.getMethod());
			addAttribute(attributes, "Request-Path", request.getPath());
			addAttribute(attributes, "Request-RemoteHost", request.getHost());
			int port = request.getPort();
			addAttribute(attributes, "Request-RemotePort", port);
		}
	}

	public static void addStatus(Map<String, Object> attributes, Response response) {
		if (response != null) {
			addAttribute(attributes, "Response-StatusCode", response.getStatus());
			addAttribute(attributes, "Response-StatusReason", response.getReason());
		}
	}

	public static void addResponse(Map<String, Object> attributes, Response response) {
		if (response != null) {
			addStatus(attributes, response);
		}
	}

	public static void setTransactionName(Request request) {
		String path = request.getPath();
		if (path != null) {
			if (path.equals("") || path.equals("/")) {
				path = "Root";
			}
			NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_LOW, false,
					"JettyClient", "Jetty", "Request", path);
		}
	}

	public static Map<String, Object> getAttributes(Object obj) {
		HashMap<String, Object> attributes = new HashMap<>();
		if (obj instanceof Request) {
			HashMap<String, Object> requestAttributes = new HashMap<>();
			addRequest(requestAttributes, (Request) obj);
			attributes.putAll(requestAttributes);
		}
		if (obj instanceof Response) {
			HashMap<String, Object> responseAttributes = new HashMap<>();
			addResponse(responseAttributes, (Response) obj);
			attributes.putAll(responseAttributes);
		}

		return attributes.isEmpty() ? null : attributes;
	}
}