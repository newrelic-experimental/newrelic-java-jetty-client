package com.newrelic.instrumentation.labs.jetty.client;

import java.util.Map;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.InboundHeaders;

public class ResponseWrapper implements InboundHeaders {
	
	private Map<String, String> headers = null;
	
	public ResponseWrapper( Map<String, String> h) {
		headers = h;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public String getHeader(String name) {
		if(headers != null) {
			return headers.get(name);
		}
		return null;
	}


}
