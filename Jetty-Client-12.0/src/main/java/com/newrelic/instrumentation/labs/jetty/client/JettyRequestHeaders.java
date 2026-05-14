package com.newrelic.instrumentation.labs.jetty.client;

import java.util.*;

import org.eclipse.jetty.client.transport.HttpRequest;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpFields;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.Headers;


public class JettyRequestHeaders implements Headers {

	private HttpFields.Mutable headerfields = null;
	private HttpRequest request = null;

	public JettyRequestHeaders(HttpFields.Mutable mutable) {
		headerfields = mutable;
	}

	public JettyRequestHeaders(HttpRequest request) {
		this.request = request;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public String getHeader(String name) {
		if(headerfields != null) {
			return headerfields.get(name);
		}
		if(request != null) {
			return request.getHeaders().get(name);
		}
		return null;
	}

	@Override
	public Collection<String> getHeaders(String name) {
        if (headerfields != null) {
            Enumeration<String> values = headerfields.getValues(name);
            if (values == null) {
                return Collections.emptyList();
            }
            return Collections.list(values);
        }
		if(request != null) {
			return request.getHeaders().getValuesList(name);
		}
		return Collections.emptyList();
    }

	@Override
	public void setHeader(String name, String value) {
		if(headerfields != null) {
			headerfields.put(name,value);
		}
		if(request != null) {
			HttpField field = new HttpField(name, value);
			request.addHeader(field);
		}
	}

	@Override
	public void addHeader(String name, String value) {
		setHeader(name, value);
	}

	@Override
	public Collection<String> getHeaderNames() {
		Iterator<HttpField> headersIterator = null;
		if(headerfields != null) {
			headersIterator = headerfields.iterator();
		} else if(request != null) {
			headersIterator = request.getHeaders().iterator();
		}
        if (headersIterator != null) {
            Collection<String> headerNames = new ArrayList<String>();
            while (headersIterator.hasNext()) {
                headerNames.add(headersIterator.next().getName());
            }
            return headerNames;
        }
		return Collections.emptyList();
    }

	@Override
	public boolean containsHeader(String name) {
		return getHeaderNames().contains(name);
	}

}