package org.smarthome.hub.example;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.smarthome.sdk.hub.producer.DeviceMessageCallback;
import org.smarthome.sdk.hub.producer.HubProducer;
import org.smarthome.sdk.hub.producer.HubProducerConfiguration;
import org.smarthome.sdk.hub.producer.HubProducerException;
import org.smarthome.sdk.models.DeviceDisconnectionDetails;
import org.smarthome.sdk.models.MessageAction;

import java.io.IOException;
import java.util.Properties;

public class Main {

    public static HubProducer producer;

    public static void main(final String[] args) {

        try {
            var producerProperties = new Properties();
            producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            var producerConfiguration = new HubProducerConfiguration(
                    "hubs-messages", "java-hub-sample", producerProperties, "test-hub"
            );
            producer = new HubProducer(producerConfiguration);
        } catch (HubProducerException e) {
            System.out.println(e.getMessage());
            return;
        }

        var testSensor = new TestDevice("test-device", new DeviceMessageCallback(producer));

        try {
            producer.registerDevice(testSensor);
            producer.setHeartBeatData("1");
        } catch (HubProducerException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("Hit Enter to terminate...");
            System.in.read();
            System.out.println("Hub is shutting down ....");

            testSensor.stop();
            producer.send(MessageAction.DEVICE_DISCONNECTED,
                    new DeviceDisconnectionDetails(testSensor.getId(), "hub is going to shutdown")
            );
            producer.stop("user requested shutdown", null, (event, ex)-> System.out.println(event));

        } catch (HubProducerException | IOException e) {
            throw new RuntimeException(e);
        }

    }

}