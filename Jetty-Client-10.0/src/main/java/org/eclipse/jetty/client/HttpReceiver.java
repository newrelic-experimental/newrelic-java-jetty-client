package org.eclipse.jetty.client;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type = MatchType.BaseClass)
public abstract class HttpReceiver {

	@Trace
	public boolean abort(HttpExchange exchange, Throwable failure) {
		// Report the error to New Relic
		NewRelic.noticeError(failure);

		// Call the original method implementation
		return Weaver.callOriginal();
	}

	@Trace
	protected void receive() {
		// Should we use getClass().getSimpleName() ?
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom", "JettyClient", "HttpReceiver",
				getClass().getSimpleName(), "receive");

		// Call the original method implementation
		Weaver.callOriginal();
	}
}
