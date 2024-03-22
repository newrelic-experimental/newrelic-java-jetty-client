package org.eclipse.jetty.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.io.Buffer;

import com.newrelic.api.agent.HttpParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.jetty.client.ResponseWrapper;

@Weave(type=MatchType.BaseClass)
public abstract class HttpExchange {

	@NewField
	public Token token = null;
	@NewField
	private Map<String,String> responseHeaders = new HashMap<String, String>();
	@NewField
	public Segment segment = null;
	
	public abstract void addRequestHeader(String name, String value);
	public abstract Address getAddress();
	public abstract Buffer getScheme();
	public abstract String getRequestURI();
	
	@Trace(async=true)
	protected void onResponseComplete() {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		Address address = getAddress();
		String theScheme = new String(getScheme().asArray());
		StringBuffer sb = new StringBuffer();
		if(theScheme != null && !theScheme.isEmpty()) {
			sb.append(theScheme);
			sb.append("://");
		}
		if(address != null) {
			String host = address.getHost();
			int port = address.getPort();
			if(host != null && !host.isEmpty()) {
				sb.append(host);
			}
			if(port > 0) {
				sb.append(':');
				sb.append(port);
			}
		}
		if(sb.length() != 0) {
			sb.append('/');
		}
		String requestPath = getRequestURI();
		if(requestPath != null && !requestPath.isEmpty()) {
			sb.append(requestPath);
		}
		URI uri = null;
		String uriString = sb.toString();
		if(!uriString.isEmpty()) {
			uri = URI.create(uriString);
		}
		ResponseWrapper inboundHeaders = new ResponseWrapper(responseHeaders);
		HttpParameters params = HttpParameters.library("JettyClient").uri(uri).procedure("send").inboundHeaders(inboundHeaders).build();
		if(segment != null) {
			segment.reportAsExternal(params);
			segment.end();
			segment = null;
		} else {
			NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
		}
		Weaver.callOriginal();
	}
	
	protected void onResponseHeader(Buffer name, Buffer value) {
		byte[] nameContents = name.asArray();
		String headerName = new String(nameContents);
		
		byte[] valueContents = value.asArray();
		String valueString = new String(valueContents);
		responseHeaders.put(headerName, valueString);
		Weaver.callOriginal();
	}
	
	protected void onConnectionFailed(Throwable x) {
		NewRelic.noticeError(x);
		if(token != null) {
			token.expire();
			token = null;
		}
		Weaver.callOriginal();
	}
	
	protected void onException(Throwable x) {
		NewRelic.noticeError(x);
		if(token != null) {
			token.expire();
			token = null;
		}
		Weaver.callOriginal();
	}
	
	protected void onExpire() {
		if(token != null) {
			token.expire();
			token = null;
		}
		Weaver.callOriginal();
	}
	
	@Trace(async=true)
	protected void onRequestCommitted() {
		if(token != null) {
			token.link();
		}
		Weaver.callOriginal();
	}
}

