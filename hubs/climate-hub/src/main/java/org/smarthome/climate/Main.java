package org.smarthome.climate;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.smarthome.sdk.hub.consumer.HubConsumer;
import org.smarthome.sdk.hub.consumer.HubConsumerConfiguration;
import org.smarthome.sdk.hub.consumer.HubConsumerException;
import org.smarthome.sdk.hub.producer.DeviceMessageCallback;
import org.smarthome.sdk.hub.producer.HubProducer;
import org.smarthome.sdk.hub.producer.HubProducerConfiguration;
import org.smarthome.sdk.hub.producer.HubProducerException;
import org.smarthome.sdk.models.DeviceDisconnectionDetails;
import org.smarthome.sdk.models.MessageAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

    public static String hubId = "smart-home-climate-hub";

    public static HubProducer producer;
    public static HubConsumer consumer;

    public static ClimateHubCommandsHandler commandsHandler;

    public static void main(String[] args) {

        try {
            setup();
        } catch (HubProducerException | HubConsumerException e) {
            System.out.println(e.getMessage());
            return;
        }

        var callback = new DeviceMessageCallback(producer);

        var thermometer = new PlainThermometer(
                "device1",
                TemperatureUnit.celsius,
                -20,
                40,
                0.5D,
                callback,
                new ImitationPattern(1, 6, 2)
        );
        commandsHandler.setDevices(new ArrayList<>(List.of(thermometer)));


        try {
            producer.registerDevice(thermometer);
            producer.setHeartBeatData("1");
        } catch (HubProducerException e) {
            System.out.println(e.getMessage());
        }


        try {
            System.out.println("Hit Enter to terminate...");
            System.in.read();
            System.out.println("Hub is shutting down ....");

            thermometer.stop();
            producer.send(MessageAction.DEVICE_DISCONNECTED, new DeviceDisconnectionDetails(
                    thermometer.getId(),
                    "hub is going to shutdown")
            );

            producer.stop("user requested shutdown", null, (event, ex)-> System.out.println(event));

        } catch (HubProducerException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void setup() throws HubProducerException, HubConsumerException{

        var producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        var producerConfiguration = new HubProducerConfiguration("hubs-messages", hubId, producerProperties, "climate-hub");
        producer = new HubProducer(producerConfiguration);

        commandsHandler = new ClimateHubCommandsHandler(producer);

        var consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, hubId);
        var consumerConfiguration = new HubConsumerConfiguration("modules-messages", hubId, consumerProperties, commandsHandler);
        consumer = new HubConsumer(consumerConfiguration);
    }


}