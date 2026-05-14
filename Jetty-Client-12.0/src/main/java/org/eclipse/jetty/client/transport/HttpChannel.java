package org.eclipse.jetty.client.transport;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type = MatchType.BaseClass)
public abstract class HttpChannel {

	@Trace
	public void send(HttpExchange exchange) {
		// Set a custom metric name for the traced method
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom", "JettyClient", "HttpChannel",
				getClass().getSimpleName(), "send");

		// Call the original implementation
		Weaver.callOriginal();
	}
}