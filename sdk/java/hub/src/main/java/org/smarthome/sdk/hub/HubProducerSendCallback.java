package org.smarthome.sdk.hub;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class HubProducerSendCallback implements Callback {

    private RecordMetadata metadata;
    private Exception exception;


    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        this.metadata = metadata;
        this.exception = exception;
    }

    public RecordMetadata getMetadata() {
        return metadata;
    }

    public Exception getException() {
        return exception;
    }
}
