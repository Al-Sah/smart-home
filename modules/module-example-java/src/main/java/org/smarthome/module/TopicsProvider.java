package org.smarthome.module;

public class TopicsProvider {

    private final String[] topics;

    public TopicsProvider(String[] topics) {
        this.topics = topics;
    }

    public String[] get() {
        return topics;
    }


}