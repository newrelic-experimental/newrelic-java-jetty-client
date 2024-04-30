package org.eclipse.jetty.client;

import java.net.URI;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.logging.Level;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Result;

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.HttpParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type = MatchType.BaseClass)
public abstract class HttpExchange {

	@NewField
	public Segment segment = null;

	public abstract Request getRequest();

	@Trace
	private AtomicMarkableReference<Result> complete(int code, Throwable failure) {

		if (failure != null) {
			NewRelic.noticeError(failure);

			if (segment != null) {
				segment.ignore();
				segment = null;
			}
		}

		URI sUri = getRequest().getURI();

		ExternalParameters params = HttpParameters.library("JettyClient").uri(sUri).procedure("complete")
				.noInboundHeaders().build();

		if (segment != null) {

			NewRelic.getAgent().getLogger().log(Level.FINEST, "nrlabs:Inside complete Method" + "/"
					+ getClass().getSimpleName() + "/" + getRequest().getPath() + "Reporting as External");

			if (params != null) {
				segment.reportAsExternal(params);
			}
			segment.end();
			segment = null;
		} else if (params != null) {
			NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
		}

		return Weaver.callOriginal();
	}
}
