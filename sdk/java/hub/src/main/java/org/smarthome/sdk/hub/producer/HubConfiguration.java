package org.smarthome.sdk.hub.producer;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Model {@code HubConfiguration} configuration.
 * @author Al-Sah
 */
public class HubConfiguration {

    private final String hubId; // this hub uuid
    private final String topic; // kafka topic
    private final Properties properties; // kafka properties


    // Heart beat properties
    private final Integer heartBeatPeriod;
    private final TimeUnit heartBeatUnit;


    // 'Key' and 'Partition' are used in ProducerRecord
    private String recordKey;
    private Integer topicPartition;

    private final String hubName;

    /**
     * Create safety producer configuration.
     * <p>Use this constructor to specify all fields overriding default values<p/>
     * Properties example:
     * <pre>{@code
     *  var properties = new Properties();
     *  properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");}
     * </pre>
     *
     * @param name       hub name
     * @param topic      topic where all messages will be sent
     * @param hubId      hub uuid; Cannot be changed after initialization
     * @param properties kafka properties.
     * @param period     'heart beat' message period
     * @param unit       'heart beat' time unit (Seconds/Minutes)
     * @param partition  ProducerRecord partition (can be null)
     * @param key        ProducerRecord key (can be null)
     * @throws IllegalArgumentException required params are invalid
     */
    public HubConfiguration(
            String topic,
            String hubId,
            Properties properties,
            Integer period,
            TimeUnit unit,
            Integer partition,
            String key,
            String name
            ) throws IllegalArgumentException {
        this.hubName = name;
        this.topic = topic;
        this.hubId = hubId;
        this.properties = properties;
        this.heartBeatPeriod = period;
        this.heartBeatUnit = unit;
        this.topicPartition = partition;
        this.recordKey = key;
        validate();
    }

    /**
     * Create safety producer configuration. <br/>
     * Use this constructor to specify all required fields and use defaults in other
     *
     * @param name       hub name
     * @param topic      topic where all messages will be sent
     * @param hubId      hub uuid; Cannot be changed after initialization
     * @param properties kafka properties.
     * @throws IllegalArgumentException required params are invalid
     */
    public HubConfiguration(String topic, String hubId, Properties properties, String name) throws IllegalArgumentException {

        this.topic = topic;
        this.hubId = hubId;
        this.properties = properties;
        this.heartBeatPeriod = 1;
        this.heartBeatUnit = TimeUnit.MINUTES;
        this.hubName = name;
        validate();
    }


    // TODO do it better ?
    private void validate() throws IllegalArgumentException{
        var sb = new StringBuilder();
        if(topic == null || topic.isBlank()){
            sb.append("\nfield 'topic' is null or blank");
        }
        if(hubId == null || hubId.isBlank()){
            sb.append("\nfield 'hub-id' is null or blank");
        }
        if(properties == null || properties.isEmpty()){
            sb.append("\nfield 'properties' is null or empty");
        }
        if(heartBeatPeriod == null || heartBeatPeriod <= 0){
            sb.append("\nfield 'heart beat period' is null or invalid");
        }
        if(heartBeatUnit == null){
            sb.append("\nfield 'heart beat unit' is null");
        }
        if(hubName == null || hubName.isBlank()){
            sb.append("\nfield 'hub-name' is null or blank");
        }
        var result = sb.toString();
        if(!result.isEmpty()){
            throw new IllegalArgumentException("invalid configuration; errors: \n" + result);
        }
    }

    // Get methods for the 'final' fields
    public Integer getHeartBeatPeriod() {
        return heartBeatPeriod;
    }

    public String getTopic() {
        return topic;
    }

    public String getHubId() {
        return hubId;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getRecordKey() {
        return recordKey;
    }

    public String getHubName() {
        return hubName;
    }



    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }

    public TimeUnit getHeartBeatUnit() {
        return heartBeatUnit;
    }
    public Integer getTopicPartition() {
        return topicPartition;
    }

    public void setTopicPartition(Integer topicPartition) {
        this.topicPartition = topicPartition;
    }
}
