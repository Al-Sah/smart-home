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

    /**
     * Creates safety producer configuration
     *
     * @param topic kafka topic
     * @param partition kafka partition (can be null)
     * @param key kafka key  (can be null)
     *
     * @param id producer UUID; Cannot be changed after initialization
     * @param properties kafka properties. <br/>Sample:
     * <pre>{@code
     *          var properties = new Properties();
     *          properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");}
     * </pre>
     *
     * @throws InvalidModelParams required params are invalid
     */
    public ProducerConfiguration(String topic, String id, Properties properties, int partition, String key) throws InvalidModelParams {

        // TODO replace validation with regex
        if(topic == null || topic.equals("")){
            throw new InvalidModelParams("field 'topic' is null or empty");
        }
        if(id == null || id.equals("")){
            throw new InvalidModelParams("field 'id' is null or empty");
        }

        if(properties == null || properties.isEmpty()){
            throw new InvalidModelParams("field 'properties' is null or empty");
        }

        this.topic = topic;
        this.id = id;
        this.kafkaProperties = properties;
        this.kafkaPartition = partition;
        this.kafkaKey = key;
    }


    public void setTopic(String topic) throws InvalidModelParams{
        if(topic == null || topic.equals("")){
            throw new InvalidModelParams("field 'topic' is null or empty");
        }
        this.topic = topic;
    }

    public void setKafkaProperties(Properties properties) throws InvalidModelParams{
        if(properties == null || properties.isEmpty()){
            throw new InvalidModelParams("field 'properties' is null or empty");
        }
        this.kafkaProperties = properties;
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