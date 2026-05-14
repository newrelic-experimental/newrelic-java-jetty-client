package org.eclipse.jetty.client;


import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.jetty.client.JettyRequestHeaders;
import com.newrelic.instrumentation.labs.jetty.client.NRCompleteListener;

@Weave(type = MatchType.BaseClass)
public abstract class HttpConnection {

	@Trace
	public void send(Request request, Response.CompleteListener listener) {

		JettyRequestHeaders headers = new JettyRequestHeaders(request);
		Transaction transaction = NewRelic.getAgent().getTransaction();

		transaction.insertDistributedTraceHeaders(headers);

		Segment segment = transaction.startSegment("Custom/" + "JettyClient" + "/" + getClass().getSimpleName() + "/" + "send");

		NRCompleteListener wrapper = new NRCompleteListener(listener, segment);
		

		Weaver.callOriginal();
	}
}