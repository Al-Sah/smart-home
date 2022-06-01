package org.smarthome.sdk.hub.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.hub.device.Device;
import org.smarthome.sdk.models.*;

import java.util.Date;
import java.util.concurrent.*;

/**
 * {@code HubProducer} is used to set up KafkaProducer and send data to the Broker
 *
 * @see HubMessage
 * @see HubProducerConfiguration
 * @author  Al-Sah
 */
public class HubProducer {

    private final HubProducerConfiguration configuration;
    private final KafkaProducer<String, HubMessage<?>> producer;
    private static final Logger logger = LoggerFactory.getLogger(HubProducer.class);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    /**
     * Additional data can be sent on 'heart beat'
     */
    private HubHeartBeatData heartBeatData;

    /**
     * @param heartBeatData data that will be sending in 'heart-beat' message;
     * This data is used by local thread
     */
    public void setHeartBeatData(HubHeartBeatData heartBeatData) {
        this.heartBeatData = heartBeatData;
    }

    /**
     * Create HubProducer. <br>
     * Thread execution is blocked until the connection is established or the timeout expires.
     * On fail HubProducerException will be thrown
     * @param configuration HubProducer configuration
     * @throws HubProducerException :configuration is null | invalid startup message | timeout expired
     */
    public HubProducer(HubProducerConfiguration configuration) throws HubProducerException {

        if(configuration == null){
            throw new HubProducerException("Configuration is null");
        }
        this.configuration = configuration;

        var prop = configuration.getProperties();
        this.producer = new KafkaProducer<>(prop, new StringSerializer(), new HubMessageSerializer());

        sendInitialMessage();

        var hb = configuration.getHeartBeatPeriod();
        if(configuration.getHeartBeatPeriod() > 0){
            scheduler.scheduleAtFixedRate(this::sendHeartBeat, hb, hb, configuration.getHeartBeatUnit());
        }
    }

    /**
     * Thread execution is blocked until the connection is established or the timeout expires
     * @throws HubProducerException failed to connect
     */
    private void sendInitialMessage() throws HubProducerException {

        var callback = new HubProducerSendCallback(MessageAction.HUB_START);
        try {
            logger.debug("Sending 'hub-start' message");
            this.send(
                    MessageAction.HUB_START,
                    new HubProperties(
                            configuration.getHubName(),
                            configuration.getHeartBeatPeriod(),
                            configuration.getHeartBeatUnit().toString()
                    ),
                    callback
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new HubProducerException(e.getMessage());
        }
        if(callback.getException() != null){
            throw new HubProducerException(callback.getException().getMessage());
        }
    }

    /**
     * Sends 'device-connected' message
     * @throws HubProducerException sending error
     */
    public void registerDevice(Device device) throws HubProducerException {
        try {
            this.send(
                    MessageAction.DEVICE_CONNECTED,
                    DTOFactory.getDeviceMetadata(device),
                    new HubProducerSendCallback(MessageAction.DEVICE_CONNECTED)
            );
        } catch (HubProducerException e) {
            logger.error(e.getMessage());
        }
    }


    /**
     * Generates 'heart-beat' message; <br>
     * Scheduler calls this function automatically in the background
     */
    private void sendHeartBeat(){
        try {
            send(MessageAction.HEART_BEAT, heartBeatData, new HubProducerSendCallback(MessageAction.HEART_BEAT));
        } catch (HubProducerException e) {
            logger.error(e.getMessage());
        }
    }


    /**
     *  Generate and asynchronously send record with specified {@link HubMessage} to kafka broker and
     *  invoke the provided callback when 'producer.send(record, callback)' has been acknowledged.
     *
     * @param action message action
     * @param data data that will be sent
     * @param callback user callback (can be null)
     */
    public Future<RecordMetadata> send(MessageAction action, Object data, Callback callback) throws HubProducerException {

        if(action == null || data == null){
            throw new HubProducerException("Invalid message");
        }

        var record = new ProducerRecord<String, HubMessage<?>>(
                configuration.getTopic(),
                configuration.getTopicPartition(),
                new Date().getTime(),
                configuration.getRecordKey(),
                new HubMessage<>(configuration.getHubId(), action.name, data)
        );

        try {
            return producer.send(record, callback);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new HubProducerException("Failed to send message; " + e.getMessage());
        }
    }
    public void send(MessageAction action, Object data) throws HubProducerException {
        send(action, data, new HubProducerSendCallback(action));
    }


    /**
     * Stop hub producer
     * @param reason shutdown reason
     * @param details shutdown details
     * @param callback user callback on 'hub-shutdown' message
     * @throws HubProducerException failed to stop KafkaProducer
     */
    public void stop(String reason, String details, Callback callback) throws HubProducerException{
        try {
            scheduler.shutdown();
            send(MessageAction.HUB_OFF, new HubShutdownDetails(reason, details), callback);
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
    public HubProducerConfiguration getConfiguration() {
        return configuration;
    }
}
