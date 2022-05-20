package org.smarthome.sdk.hub;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.hub.serialization.HubMessageSerializer;
import org.smarthome.sdk.models.HubMessage;
import org.smarthome.sdk.models.MessageAction;
import org.smarthome.sdk.models.json.HubMessageMapper;
import org.smarthome.sdk.models.json.JsonHubMessage;

import java.util.Date;
import java.util.concurrent.*;

/**
 * {@code HubProducer} is used to set up KafkaProducer and send data to the Broker
 *
 * @see HubMessage
 * @see HubConfiguration
 * @author  Al-Sah
 */
public class HubProducer {

    private final HubConfiguration configuration;
    private final KafkaProducer<String, JsonHubMessage> producer;
    private static final Logger logger = LoggerFactory.getLogger(HubProducer.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    /**
     * Additional data can be sent on 'heart beat'
     * Must be in JSON format
     */
    private String HearBeatData;

    /**
     * @param hearBeatData data that will be sending in 'heart-beat' message;
     * This data is used by local thread
     */
    public void setHearBeatData(String hearBeatData) {
        HearBeatData = hearBeatData;
    }

    /**
     * Create HubProducer. <br>
     * Thread execution is blocked until the connection is established or the timeout expires.
     * On fail HubProducerException will be thrown
     * @param configuration HubProducer configuration
     * @param setup Initial message with action {@code HubMessage.Action.HUB_START}
     * @throws HubProducerException :configuration is null | invalid startup message | timeout expired
     */
    public HubProducer(HubConfiguration configuration, HubMessage<String> setup) throws HubProducerException {

        if(configuration == null){
            throw new HubProducerException("Configuration is null");
        }
        this.configuration = configuration;

        var prop = configuration.getProperties();
        this.producer = new KafkaProducer<>(prop, new StringSerializer(), new HubMessageSerializer());

        sendInitialMessage(setup);

        var hb = configuration.getHeartBeatPeriod();
        if(configuration.getHeartBeatPeriod() > 0){
            scheduler.scheduleAtFixedRate(this::sendHeartBeat, hb, hb, configuration.getHeartBeatUnit());
        }
    }

    /**
     * Thread execution is blocked until the connection is established or the timeout expires
     * @param msg startup message
     * @throws HubProducerException failed to connect
     */
    private void sendInitialMessage(HubMessage<String> msg) throws HubProducerException {

        if(msg == null || msg.getAction() == null || msg.getAction() != MessageAction.HUB_START){
            throw new HubProducerException("Invalid startup message");
        }

        var callback = new HubProducerSendCallback();
        try {
            logger.debug("Sending 'hub-start' message");
            this.send(msg, callback).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new HubProducerException(e.getMessage());
        }

        if(callback.getException() != null){
            throw new HubProducerException(callback.getException().getMessage());
        }
    }

    /**
     * Generates 'heart-beat' message; <br>
     * Scheduler calls this function automatically in the background
     */
    private void sendHeartBeat(){
        try {
            send(
                    new HubMessage<>(configuration.getHubId(), MessageAction.HEART_BEAT, HearBeatData),
                    (metadata, exception) -> {
                        if(exception != null){
                            logger.error(exception.getMessage());
                        } else {
                            logger.debug(String.format("HEART BEAT: %s; TS:%s", metadata.toString(), new Date(metadata.timestamp())));
                        }
                    }
            );
        } catch (HubProducerException e) {
            logger.error(e.getMessage());
        }
    }


    /**
     *  Generate and asynchronously send record with specified {@link HubMessage} to kafka broker and
     *  invoke the provided callback when 'producer.send(record, callback)' has been acknowledged.
     *
     * @param message message that will be sent
     * @param callback user callback (can be null)
     */
    public Future<RecordMetadata> send(HubMessage<?> message, Callback callback) throws HubProducerException {

        if(message == null || message.getAction() == null){
            throw new HubProducerException("Invalid message");
        }

        var record = new ProducerRecord<>(
                configuration.getTopic(),
                configuration.getTopicPartition(),
                new Date().getTime(),
                configuration.getRecordKey(),
                HubMessageMapper.getMessage(message)
        );

        try {
            return producer.send(record, callback);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new HubProducerException("Failed to send message; " + e.getMessage());
        }
    }


    /**
     * Stop hub producer </br>
     *
     * @param data user data in the json format
     * @param callback user callback on 'hub-off' message
     * @throws HubProducerException failed to stop KafkaProducer
     */
    public void stop(String data, Callback callback) throws HubProducerException{
        try {
            scheduler.shutdown();
            send(new HubMessage<>(configuration.getHubId(), MessageAction.HUB_OFF, data), callback);
            producer.flush();
            producer.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new HubProducerException("Failed to stop producer; " + e.getMessage());
        }
    }


    /**
     * @return hub configuration
     */
    public HubConfiguration getConfiguration() {
        return configuration;
    }
}
