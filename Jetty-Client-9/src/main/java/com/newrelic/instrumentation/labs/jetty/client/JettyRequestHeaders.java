package com.newrelic.instrumentation.labs.jetty.client;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpFields;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.Headers;

public class JettyRequestHeaders implements Headers {

	private Request request = null;

	public JettyRequestHeaders(Request req) {
		request = req;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public String getHeader(String name) {

		HttpFields headers = request.getHeaders();
		if (headers != null) {
			return headers.get(name);
		}
		return null;
	} // ok.

	@Override
	public Collection<String> getHeaders(String name) {
		HttpFields headers = request.getHeaders();
		if (headers != null) {
			return headers.getValuesCollection(name);
		}
		return Collections.emptyList();
	}

	@Override
	public void setHeader(String name, String value) {
		HttpFields headers = request.getHeaders();
		headers.remove(name);
		headers.add(name, value);
	}

	@Override
	public void addHeader(String name, String value) {
		HttpFields headers = request.getHeaders();
		headers.add(name, value);
	}

	@Override
	public Collection<String> getHeaderNames() {
		HttpFields headers = request.getHeaders();

		return headers.getFieldNamesCollection();
	}

	@Override
	public boolean containsHeader(String name) {
		return getHeaderNames().contains(name);
	}

}