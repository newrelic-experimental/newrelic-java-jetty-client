package org.eclipse.jetty.util;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type = MatchType.Interface)
public abstract class Callback {

	@NewField
	public Segment segment = null;

	@Trace
	public void succeeded() {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom", "JettyClient", "Callback",
				getClass().getSimpleName(), "succeeded");
		if (segment != null) {
			segment.end();
			segment = null;
		}

		// Call the original method implementation
		Weaver.callOriginal();
	}

	@Trace
	public void failed(Throwable x) {

		NewRelic.getAgent().getTracedMethod().setMetricName("Custom", "JettyClient", "Callback",
				getClass().getSimpleName(), "failed");
		NewRelic.noticeError(x);
		if (segment != null) {
			segment.end();
			segment = null;
		}

		// Call the original method implementation
		Weaver.callOriginal();
	}
}