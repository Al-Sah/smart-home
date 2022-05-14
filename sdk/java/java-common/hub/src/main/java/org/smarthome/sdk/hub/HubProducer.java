package org.smarthome.sdk.hub;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringSerializer;

import org.smarthome.sdk.hub.serialization.HubMessageSerializer;
import org.smarthome.sdk.models.HubMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.ProducerConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private final KafkaProducer<String, HubMessage> producer;
    private static final String HUB_ID_HEADER = "hub-id";
    private static final Logger logger = LoggerFactory.getLogger(HubProducer.class);

    /**
     *
     * @param configuration HubProducer configuration
     * @param startMessage Initial message with action {@code HubMessage.Action.HUB_START}
     * @param callback A user-supplied callback to execute when the record has been acknowledged by the server (null indicates no callback)
     * @throws HubProducerException
     * <ul>
     *     <li>Configuration is null</li>
     *     <li>Invalid startup message</li>
     * </ul>
     */
    public HubProducer(ProducerConfiguration configuration, HubMessage startMessage, Callback callback) throws HubProducerException {

        if(configuration == null){
            throw new HubProducerException("Configuration is null");
        }
        if(startMessage == null || startMessage.getAction() == null || startMessage.getAction() != HubMessage.Action.HUB_START){
            throw new HubProducerException("Invalid startup message");
        }

        this.configuration = configuration;
        this.hubHeaders = new ArrayList<>(List.of(
                new RecordHeader(HUB_ID_HEADER, configuration.getId().getBytes(StandardCharsets.UTF_8)))
        );

        this.producer = new KafkaProducer<>(
                configuration.getKafkaProperties(),
                new StringSerializer(),
                new HubMessageSerializer()
        );

        this.send(startMessage, callback);
    }

    /**
     *  Generate and asynchronously send record with specified {@link HubMessage} to kafka broker and
     *  invoke the provided callback when 'producer.send(record, callback)' has been acknowledged.
     */
    public void send(HubMessage message, Callback callback) throws HubProducerException {

        if(message == null || message.getAction() == null){
            throw new HubProducerException("Invalid message");
        }

        var record = new ProducerRecord<>(
                configuration.getTopic(),
                configuration.getKafkaPartition(),
                new Date().getTime(),
                configuration.getKafkaKey(),
                message,
                hubHeaders
        );

        try {
            producer.send(record, callback);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new HubProducerException("Failed to send message; " + e.getMessage());
        }
    }


    /**
     *  Stop hub producer. </br>
     */
    public void stop(Callback callback) throws HubProducerException{
        try {
            send(new HubMessage(HubMessage.Action.HUB_OFF, null), callback);
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

    public KafkaProducer<String, HubMessage> getProducer() {
        return producer;
    }

}
