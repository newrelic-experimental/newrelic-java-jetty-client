package com.newrelic.instrumentation.labs.jetty.client;

import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;

import com.newrelic.api.agent.Segment;

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
