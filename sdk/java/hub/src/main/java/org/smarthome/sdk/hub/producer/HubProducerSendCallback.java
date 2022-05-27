package org.smarthome.sdk.hub.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.MessageAction;

import java.util.Date;

public class HubProducerSendCallback implements Callback {

    private static final Logger logger = LoggerFactory.getLogger(HubProducerSendCallback.class);
    private RecordMetadata metadata;
    private Exception exception;
    private final String action;

    public HubProducerSendCallback(MessageAction action){
        this.action = action.toString();
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        this.metadata = metadata;
        this.exception = exception;

        if(exception != null){
            logger.error(exception.getMessage());
        } else {
            logger.debug(String.format(" %s: %s | %s", action, metadata.toString(), new Date(metadata.timestamp())));
        }
    }

    public RecordMetadata getMetadata() {
        return metadata;
    }

    public Exception getException() {
        return exception;
    }
}
