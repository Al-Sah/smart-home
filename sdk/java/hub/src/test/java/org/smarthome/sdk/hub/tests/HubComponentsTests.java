package org.smarthome.sdk.hub.tests;

import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smarthome.sdk.hub.consumer.HubConsumer;
import org.smarthome.sdk.hub.consumer.HubConsumerConfiguration;
import org.smarthome.sdk.hub.producer.HubMessageSerializer;
import org.smarthome.sdk.hub.producer.HubProducer;
import org.smarthome.sdk.hub.producer.HubProducerConfiguration;

import java.util.Properties;

public class HubComponentsTests {

    @Test
    public void testHubProducerCreationAndStop(){

        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        var producer = Assertions.assertDoesNotThrow(()->
                new HubProducer(
                        new HubProducerConfiguration("test-topic", "test-hub", properties, "climate-hub"),
                        new MockProducer<>(true, new StringSerializer(), new HubMessageSerializer())));
        Assertions.assertDoesNotThrow(()-> producer.stop("test-end", "", null));
    }

    @Test
    public void testHubConsumerCreationAndStop(){

        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        var consumer = Assertions.assertDoesNotThrow(()->
                new HubConsumer(
                        new HubConsumerConfiguration("test-topic", "test-hub", properties, command -> {}),
                        new MockConsumer<>(OffsetResetStrategy.EARLIEST))
        );
        Assertions.assertDoesNotThrow(consumer::stop);
    }
}
