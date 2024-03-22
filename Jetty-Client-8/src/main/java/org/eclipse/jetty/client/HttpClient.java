package org.eclipse.jetty.client;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.jetty.client.RequestWrapper;

@Weave
public class HttpClient {

	@Trace
	public void send(HttpExchange exchange) {
		exchange.token = NewRelic.getAgent().getTransaction().getToken();
		exchange.segment = NewRelic.getAgent().getTransaction().startSegment("JettyClientSend");
		RequestWrapper wrapper = new RequestWrapper(exchange);
		NewRelic.getAgent().getTracedMethod().addOutboundRequestHeaders(wrapper);
		Weaver.callOriginal();
	}
}
