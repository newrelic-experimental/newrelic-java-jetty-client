package org.eclipse.jetty.client;

import java.util.List;
import java.util.logging.Level;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.jetty.client.JettyRequestHeaders;

@Weave(type = MatchType.BaseClass)
public class HttpClient {

	@Trace
	protected void send(final Request request, List<Response.ResponseListener> listeners) {

		NewRelic.getAgent().getLogger().log(Level.FINEST, "nrlabs:Inside send Method" + "/" + getClass().getSimpleName()
				+ "/" + request.getPath() + "Inserted DT Headers");

		NewRelic.getAgent().getTracedMethod()
				.setMetricName(new String[] { "Custom", "JettyClient", getClass().getSimpleName(), "send" });

		JettyRequestHeaders headers = new JettyRequestHeaders(request);
		Transaction transaction = NewRelic.getAgent().getTransaction();

		transaction.insertDistributedTraceHeaders(headers);

		Weaver.callOriginal();
	}
}
