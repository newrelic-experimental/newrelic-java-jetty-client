package org.eclipse.jetty.client.transport;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import org.eclipse.jetty.util.Promise;

@Weave(type = MatchType.BaseClass)
public abstract class HttpReceiver {

	@Trace
	public void abort(HttpExchange exchange, Throwable failure, Promise<Boolean> promise) {
		// Report the error to New Relic
		NewRelic.noticeError(failure);

		// Call the original method implementation
		Weaver.callOriginal();
	}

}
