package org.eclipse.jetty.client;

import java.net.URI;
import java.nio.ByteBuffer;

import org.eclipse.jetty.util.Callback;

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.HttpParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type = MatchType.BaseClass)
public abstract class HttpSender {

	@Trace
	public boolean abort(HttpExchange exchange, Throwable failure) {
		// Report the error to New Relic
		NewRelic.noticeError(failure);

		// Call the original method implementation
		return Weaver.callOriginal();
	}

	@Trace
	public void send(HttpExchange exchange) {
		// Extract relevant information from HttpExchange
		HttpRequest request = exchange.getRequest();
		URI uri = request.getURI();
		String method = request.getMethod();

		// Create and report external parameters
		if (uri != null && method != null) {
			ExternalParameters params = HttpParameters.library("JettyClient").uri(uri).procedure(method)
					.noInboundHeaders().build();

			NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
		}

		// Call the original method implementation
		Weaver.callOriginal();
	}

	@Trace
	protected void sendHeaders(HttpExchange exchange, ByteBuffer contentBuffer, boolean lastContent,
			Callback callback) {
		callback.segment = NewRelic.getAgent().getTransaction()
				.startSegment("Custom/JettyClient/" + getClass().getSimpleName() + "/sendHeaders");

		// Call the original method implementation
		Weaver.callOriginal();

		// End the segment after the original method call
		if (callback.segment != null) {
			callback.segment.end();
			callback.segment = null;
		}
	}

	@Trace
	protected void sendContent(HttpExchange exchange, ByteBuffer contentBuffer, boolean lastContent,
			Callback callback) {
		callback.segment = NewRelic.getAgent().getTransaction()
				.startSegment("Custom/JettyClient/" + getClass().getSimpleName() + "/sendContent");

		// Call the original method implementation
		Weaver.callOriginal();

		// End the segment after the original method call
		if (callback.segment != null) {
			callback.segment.end();
			callback.segment = null;
		}
	}
}