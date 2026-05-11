package com.newrelic.instrumentation.labs.jetty.client;

import com.newrelic.api.agent.Segment;
import org.eclipse.jetty.client.Response;
import org.eclipse.jetty.client.Result;

public class NRCompleteListener implements Response.CompleteListener {

	private Response.CompleteListener delegate = null;

	private Segment segment = null;

	@Override
	public void onComplete(Result result) {
		// TODO Auto-generated method stub
		if (delegate != null) {

			delegate.onComplete(result);
		}

		if (segment != null) {
			segment.end();
			segment = null;
		}

	}

	public NRCompleteListener(Response.CompleteListener oCompleteListener, Segment segment) {
		// Initialized.
		delegate = oCompleteListener;
		this.segment = segment;

	}

}
