package org.eclipse.jetty.client;

import java.util.Map;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.jetty.client.Utils;

@Weave(type = MatchType.Interface)
public abstract class ProtocolHandler {

	public abstract String getName();

	@Trace
	public boolean accept(Request request, Response response) {
		// Should we use getClass().getSimpleName() ?
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom", "JettyClient", "ProtocolHandler",
				getClass().getSimpleName(), getName(), "accept");

		// Capture attributes from Request and Response using Utils
		Map<String, Object> attributes = Utils.getAttributes(request);
		if (response != null) {
			Map<String, Object> responseAttributes = Utils.getAttributes(response);
			if (responseAttributes != null) {
				attributes.putAll(responseAttributes);
			}
		}

		// Add attributes to the traced method
		if (attributes != null && !attributes.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		}

		return Weaver.callOriginal();
	}
}