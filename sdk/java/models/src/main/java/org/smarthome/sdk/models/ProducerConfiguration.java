package org.smarthome.sdk.models;

import java.util.Properties;

/**
 * Model {@code ProducerConfiguration} is used to describe {@code HubProducer} or {@code ModuleProducer} configuration.
 * @author  Al-Sah
 */
public class ProducerConfiguration {

    private final String id;
    private String topic;
    private int kafkaPartition;
    private String kafkaKey;
    private Properties kafkaProperties;
    private Integer heartBeatPeriod;

    /**
     * Creates safety producer configuration
     *
     * @param topic kafka topic
     * @param partition kafka partition
     * @param period heart beat HubMessage period; if period = 0 -> heart beat will not be sending
     * @param key kafka key  (can be null)
     *
     * @param id producer UUID; Cannot be changed after initialization
     * @param properties kafka properties. <br/>Sample:
     * <pre>{@code
     *          var properties = new Properties();
     *          properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");}
     * </pre>
     *
     * @throws IllegalArgumentException required params are invalid
     */
    // TODO use Builder pattern ??
    public ProducerConfiguration(String topic, String id, Properties properties, int partition, int period, String key) throws IllegalArgumentException {

        // TODO replace validation with regex
        if(topic == null || topic.equals("")){
            throw new IllegalArgumentException("field 'topic' is null or empty");
        }
        if(id == null || id.equals("")){
            throw new IllegalArgumentException("field 'id' is null or empty");
        }

        if(properties == null || properties.isEmpty()){
            throw new IllegalArgumentException("field 'properties' is null or empty");
        }

        this.topic = topic;
        this.id = id;
        this.kafkaProperties = properties;
        this.kafkaPartition = partition;
        this.kafkaKey = key;
        this.heartBeatPeriod = period;
    }


    public void setTopic(String topic) throws IllegalArgumentException{
        if(topic == null || topic.equals("")){
            throw new IllegalArgumentException("field 'topic' is null or empty");
        }
        this.topic = topic;
    }

    public void setKafkaProperties(Properties properties) throws IllegalArgumentException{
        if(properties == null || properties.isEmpty()){
            throw new IllegalArgumentException("field 'properties' is null or empty");
        }
        this.kafkaProperties = properties;
    }

    public Integer getHeartBeatPeriod() {
        return heartBeatPeriod;
    }

    public void setHeartBeatPeriod(int heartBeatPeriod) {
        this.heartBeatPeriod = heartBeatPeriod;
    }


    public void setKafkaPartition(int kafkaPartition) {
        this.kafkaPartition = kafkaPartition;
    }

    public void setKafkaKey(String kafkaKey) {
        this.kafkaKey = kafkaKey;
    }

    public String getTopic() {
        return topic;
    }

    public String getId() {
        return id;
    }

    public Properties getKafkaProperties() {
        return kafkaProperties;
    }

    public int getKafkaPartition() {
        return kafkaPartition;
    }

    public String getKafkaKey() {
        return kafkaKey;
    }
}
