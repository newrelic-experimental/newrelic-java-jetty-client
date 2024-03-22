package com.newrelic.instrumentation.labs.jetty.client;

import org.eclipse.jetty.client.HttpExchange;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.OutboundHeaders;

public class RequestWrapper implements OutboundHeaders {
	
	private HttpExchange exchange;
	
	public RequestWrapper(HttpExchange ex) {
		exchange = ex;
	}
	

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public void setHeader(String name, String value) {
		if(exchange != null) {
			exchange.addRequestHeader(name, value);
		}
	}

}
