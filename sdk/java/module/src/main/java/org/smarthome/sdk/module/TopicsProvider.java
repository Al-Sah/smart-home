package org.smarthome.sdk.module;

/**
 * TopicsProvider is used in ModuleConsumer to provide KafkaListener with topics
 */
public class TopicsProvider {

    private final String[] topics;

    public TopicsProvider(String[] topics) {
        this.topics = topics;
    }

    public String[] get() {
        return topics;
    }


}