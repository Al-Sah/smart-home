package org.smarthome.sdk.hub;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;

import org.smarthome.sdk.hub.serialization.HubMessageSerializer;
import org.smarthome.sdk.models.HubMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.MessageAction;
import org.smarthome.sdk.models.json.HubMessageMapper;
import org.smarthome.sdk.models.json.JsonHubMessage;
import org.smarthome.sdk.models.ProducerConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * {@code HubProducer} is used to set up KafkaProducer and send data to the Broker
 *
 * @see HubMessage
 * @see ProducerConfiguration
 * @author  Al-Sah
 */
public class HubProducer {

    private final ProducerConfiguration configuration;
    private final List<Header> hubHeaders;
    private final KafkaProducer<String, JsonHubMessage> producer;
    private static final String HUB_ID_HEADER = "hub-id";
    private static final Logger logger = LoggerFactory.getLogger(HubProducer.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     *
     * @param configuration HubProducer configuration
     * @param startMessage Initial message with action {@code HubMessage.Action.HUB_START}
     * @throws HubProducerException
     * <ul>
     *     <li>Configuration is null</li>
     *     <li>Invalid startup message</li>
     * </ul>
     */
    public HubProducer(ProducerConfiguration configuration, HubMessage<String> startMessage) throws HubProducerException {

        if(configuration == null){
            throw new HubProducerException("Configuration is null");
        }

        this.configuration = configuration;
        this.hubHeaders = new ArrayList<>();
        hubHeaders.add(new RecordHeader(HUB_ID_HEADER, configuration.getId().getBytes(StandardCharsets.UTF_8)));

        this.producer = new KafkaProducer<>(
                configuration.getKafkaProperties(),
                new StringSerializer(),
                new HubMessageSerializer()
        );

        sendInitialMessage(startMessage);


        if(configuration.getHeartBeatPeriod() > 0){
            scheduler.scheduleAtFixedRate(
                    this::sendHeartBeat,
                    configuration.getHeartBeatPeriod(),
                    configuration.getHeartBeatPeriod(),
                    TimeUnit.SECONDS
            );
        }
    }


    private void sendInitialMessage(HubMessage<String> msg) throws HubProducerException {

        if(msg == null || msg.getAction() == null || msg.getAction() != MessageAction.HUB_START){
            throw new HubProducerException("Invalid startup message");
        }

        var callback = new HubProducerSendCallback();
        try {
            logger.debug("Sending 'HUB_START' message");
            this.send(msg, callback).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new HubProducerException(e.getMessage());
        }

        if(callback.getException() != null){
            throw new HubProducerException(callback.getException().getMessage());
        }
    }

    private void sendHeartBeat(){
        try {
            send(
                    new HubMessage<String>(MessageAction.HEART_BEAT, null),
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
     */
    public Future<RecordMetadata> send(HubMessage<?> message, Callback callback) throws HubProducerException {

        if(message == null || message.getAction() == null){
            throw new HubProducerException("Invalid message");
        }

        var record = new ProducerRecord<>(
                configuration.getTopic(),
                configuration.getKafkaPartition(),
                new Date().getTime(),
                configuration.getKafkaKey(),
                HubMessageMapper.getMessage(message),
                hubHeaders
        );

        try {
            return producer.send(record, callback);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new HubProducerException("Failed to send message; " + e.getMessage());
        }
    }


    /**
     *  Stop hub producer. </br>
     */
    public void stop(Callback callback, String reason) throws HubProducerException{
        try {
            scheduler.shutdown();
            send(new HubMessage<>(MessageAction.HUB_OFF, reason), callback);
            producer.flush();
            producer.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new HubProducerException("Failed to stop producer; " + e.getMessage());
        }
    }


    public ProducerConfiguration getConfiguration() {
        return configuration;
    }

    public List<Header> getHubHeaders() {
        return hubHeaders;
    }

}
