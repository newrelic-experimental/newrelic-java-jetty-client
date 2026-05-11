package org.eclipse.jetty.client;

import java.net.URI;

import com.newrelic.api.agent.weaver.Weave;

@Weave
public abstract class HttpRequest {

	public abstract URI getURI();

	public abstract String getMethod();

	// @Trace
	// private void sendAsync(BiConsumer<HttpRequest,
	// List<Response.ResponseListener>> sender,
	// Response.CompleteListener listener) {

	// }
}
