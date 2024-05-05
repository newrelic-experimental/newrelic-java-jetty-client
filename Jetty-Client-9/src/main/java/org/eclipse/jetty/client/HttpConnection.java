package org.eclipse.jetty.client;

import java.util.logging.Level;

import org.eclipse.jetty.io.EndPoint;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type = MatchType.BaseClass)
public abstract class HttpConnection {

	@NewField
	protected Token token = null;

	public HttpConnection(HttpClient client, EndPoint endPoint, HttpDestination destination) {

	}

	@Trace(async = true)
	public void send(HttpExchange exchange) {

		NewRelic.getAgent().getLogger().log(Level.FINEST, "nrlabs:Inside send Method" + "/" + getClass().getSimpleName()
				+ "/" + exchange.getRequest().getPath() + "Token Linked and Segment Started");

		if (token != null) {
			token.linkAndExpire();
			token = null;
		}
		exchange.segment = NewRelic.getAgent().getTransaction()
				.startSegment("Custom/" + "JettyClient" + "/" + getClass().getSimpleName() + "/" + "send");
		Weaver.callOriginal();
	}
}
