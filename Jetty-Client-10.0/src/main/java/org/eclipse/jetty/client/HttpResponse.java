package org.eclipse.jetty.client;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave
public abstract class HttpResponse {

	@Trace
	public boolean abort(Throwable cause) {
		// Report the error to New Relic
		NewRelic.noticeError(cause);

		// Call the original method implementation
		return Weaver.callOriginal();
	}
}