package org.eclipse.jetty.client.transport;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.jetty.client.JettyRequestHeaders;
import com.newrelic.instrumentation.labs.jetty.client.NRHeadersConsumer;
import org.eclipse.jetty.client.Request;
import org.eclipse.jetty.client.Response;

@Weave(type = MatchType.BaseClass)
public abstract class HttpDestination {

	@Trace
	public void send(Request request, Response.CompleteListener listener) {
		NRHeadersConsumer headersConsumer = new NRHeadersConsumer();
		request.headers(headersConsumer);

		Weaver.callOriginal();
	}

}
