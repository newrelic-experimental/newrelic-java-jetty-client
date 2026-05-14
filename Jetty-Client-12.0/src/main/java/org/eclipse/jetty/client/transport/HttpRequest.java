package org.eclipse.jetty.client.transport;

import java.net.URI;

import com.newrelic.api.agent.weaver.Weave;
import org.eclipse.jetty.client.Request;
import org.eclipse.jetty.http.HttpField;

@Weave
public abstract class HttpRequest implements Request {

	public abstract URI getURI();

	public abstract String getMethod();

	public abstract HttpRequest addHeader(HttpField header);

	// @Trace
	// private void sendAsync(BiConsumer<HttpRequest,
	// List<Response.ResponseListener>> sender,
	// Response.CompleteListener listener) {

	// }
}
