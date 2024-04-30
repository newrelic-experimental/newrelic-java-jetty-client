package org.eclipse.jetty.client;

import java.util.logging.Level;

import org.eclipse.jetty.client.api.Connection;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type = MatchType.BaseClass)
public abstract class HttpDestination {

	@Trace
	protected void process(Connection connection, boolean dispatch) {

		HttpConnection httpConnection = (HttpConnection) connection;

		NewRelic.getAgent().getLogger().log(Level.FINEST,
				"nrlabs:Inside process Method" + "/" + getClass().getSimpleName() + "/" + "Token assigned");

		if (dispatch) {
			// ASynch
			httpConnection.token = NewRelic.getAgent().getTransaction().getToken();
		}
		Weaver.callOriginal();
	}

}
