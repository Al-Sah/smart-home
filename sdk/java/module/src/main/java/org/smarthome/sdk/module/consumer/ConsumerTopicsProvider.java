package org.smarthome.sdk.module.consumer;

/**
 * ConsumerTopicsProvider is used in ModuleConsumer to provide KafkaListener with topics
 */
public class ConsumerTopicsProvider {

    private final String[] topics;

    public ConsumerTopicsProvider(String[] topics) {
        this.topics = topics;
    }

    public String[] get() {
        return topics;
    }


}