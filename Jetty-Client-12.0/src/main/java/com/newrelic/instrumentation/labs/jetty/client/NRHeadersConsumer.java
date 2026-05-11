package com.newrelic.instrumentation.labs.jetty.client;

import com.newrelic.api.agent.NewRelic;
import org.eclipse.jetty.http.HttpFields;

import java.util.function.Consumer;

public class NRHeadersConsumer implements Consumer<HttpFields.Mutable> {

    @Override
    public void accept(HttpFields.Mutable httpFields) {
        JettyRequestHeaders headers = new JettyRequestHeaders(httpFields);
        NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(headers);
    }
}
