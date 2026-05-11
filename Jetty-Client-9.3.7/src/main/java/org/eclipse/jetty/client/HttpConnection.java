package org.eclipse.jetty.client;

import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;

@Weave(type = MatchType.BaseClass)
public abstract class HttpConnection {

	@NewField
	protected Token token = null;

	protected HttpConnection(HttpDestination destination) {

	}

	@Trace(async = true)
	protected SendFailure send(HttpExchange exchange) {

		NewRelic.getAgent().getLogger().log(Level.FINEST, "nrlabs:Inside send Method" + "/" + getClass().getSimpleName()
				+ "/" + exchange.getRequest().getPath() + "Token Linked and Segment Started");

		if (token != null) {
			token.linkAndExpire();
			token = null;
		}
		exchange.segment = NewRelic.getAgent().getTransaction()
				.startSegment("Custom/" + "JettyClient" + "/" + getClass().getSimpleName() + "/" + "send");
		return Weaver.callOriginal();
	}

	@Trace
	public void send(Request request, Response.CompleteListener listener) {
		Weaver.callOriginal();
	}
}
