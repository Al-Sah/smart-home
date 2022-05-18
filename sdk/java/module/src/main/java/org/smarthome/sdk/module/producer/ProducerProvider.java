package org.smarthome.sdk.module.producer;


public class ProducerProvider {

    private final String topic;
    private final String key;
    private final Integer partition;

    public ProducerProvider(String topic, String key, int partition) {
        this.topic = topic;
        this.key = key;
        this.partition = partition;
    }

    public String getTopic() {
        return topic;
    }

    public String getKey() {
        return key;
    }

    public Integer getPartition() {
        return partition;
    }
}
