package org.eclipse.jetty.client.transport;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import java.util.concurrent.CompletableFuture;

@Weave
public abstract class HttpResponse {

	@Trace
	public CompletableFuture<Boolean> abort(Throwable cause) {
		// Report the error to New Relic
		NewRelic.noticeError(cause);

		// Call the original method implementation
		return Weaver.callOriginal();
	}
}