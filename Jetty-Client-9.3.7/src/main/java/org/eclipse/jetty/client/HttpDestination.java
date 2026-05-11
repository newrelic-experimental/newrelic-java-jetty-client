package org.eclipse.jetty.client;

import java.util.List;

import org.eclipse.jetty.client.api.Response;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type = MatchType.BaseClass)
public abstract class HttpDestination {

	@Trace
	protected void send(HttpRequest request, List<Response.ResponseListener> listeners) {
		Weaver.callOriginal();
	}

	@Trace
	public void send() {
		Weaver.callOriginal();
	}
}
