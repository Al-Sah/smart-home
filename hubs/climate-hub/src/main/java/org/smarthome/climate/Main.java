package org.smarthome.climate;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.smarthome.climate.devices.*;
import org.smarthome.sdk.hub.consumer.HubConsumer;
import org.smarthome.sdk.hub.consumer.HubConsumerConfiguration;
import org.smarthome.sdk.hub.consumer.HubConsumerException;
import org.smarthome.sdk.hub.producer.DeviceMessageCallback;
import org.smarthome.sdk.hub.producer.HubProducer;
import org.smarthome.sdk.hub.producer.HubProducerConfiguration;
import org.smarthome.sdk.hub.producer.HubProducerException;
import org.smarthome.sdk.models.DeviceDisconnectionDetails;
import org.smarthome.sdk.models.HubHeartBeatData;
import org.smarthome.sdk.models.MessageAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

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

        var thermometer1 = new PlainThermometer(
                "device-plain-thermometer1",
                new ThermometerSettings(
                        "plain-thermometer",
                        TemperatureUnit.celsius,
                        -20, 40, 0.5F
                ),
                new ImitationPattern(TimeUnit.SECONDS, 1, 3),
                callback
        );

        var thermometer2 = new PlainThermometer(
                "device-plain-thermometer2",
                new ThermometerSettings(
                        "plain-thermometer",
                        TemperatureUnit.fahrenheit,
                        -22, 122, 1F
                ),
                new ImitationPattern(TimeUnit.SECONDS, 2, 7),
                callback
        );

        var thermometer3 = new ConfigurableThermometer(
                "device-configurable-thermometer1",
                new ThermometerSettings(
                        "configurable-thermometer",
                        TemperatureUnit.celsius,
                        -25, 60, 0.5F
                ),
                new ImitationPattern(TimeUnit.SECONDS, 5, 10),
                callback
        );

        var luxMeter = new LuxMeter(
                "device-lux-meter1",
                new ImitationPattern(TimeUnit.SECONDS, 5, 10),
                callback
        );

        commandsHandler.setDevices(new ArrayList<>(List.of(thermometer1, thermometer2, thermometer3, luxMeter)));

        try {
            producer.registerDevice(thermometer1);
            producer.registerDevice(thermometer2);
            producer.registerDevice(thermometer3);
            producer.registerDevice(luxMeter);
            producer.setHeartBeatData(new HubHeartBeatData(new String[]{
                    thermometer1.getId(),
                    thermometer2.getId(),
                    thermometer3.getId(),
                    luxMeter.getId()
            }, null));
        } catch (HubProducerException e) {
            System.out.println(e.getMessage());
        }


        try {
            System.out.println("Hit Enter to terminate...");
            System.in.read();
            System.out.println("Hub is shutting down ....");

            thermometer1.stop();
            thermometer2.stop();
            thermometer3.stop();
            luxMeter.stop();
            producer.send(MessageAction.DEVICE_DISCONNECTED,
                    new DeviceDisconnectionDetails(thermometer1.getId(), "hub is going to shutdown")
            );
            producer.send(MessageAction.DEVICE_DISCONNECTED,
                    new DeviceDisconnectionDetails(thermometer2.getId(), "hub is going to shutdown")
            );
            producer.send(MessageAction.DEVICE_DISCONNECTED,
                    new DeviceDisconnectionDetails(thermometer3.getId(), "hub is going to shutdown")
            );
            producer.send(MessageAction.DEVICE_DISCONNECTED,
                    new DeviceDisconnectionDetails(luxMeter.getId(), "hub is going to shutdown")
            );

            producer.stop("user requested shutdown", null, (event, ex)-> System.out.println(event));
            consumer.stop();

        } catch (HubProducerException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void setup() throws HubProducerException, HubConsumerException{

        var producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        var producerConfiguration = new HubProducerConfiguration("hubs-messages", hubId, producerProperties, "climate-hub");
        producer = new HubProducer(producerConfiguration);

        commandsHandler = new ClimateHubCommandsHandler(producer);
        var consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, hubId);
        var consumerConfiguration = new HubConsumerConfiguration("modules-messages", hubId, consumerProperties, commandsHandler);
        consumer = new HubConsumer(consumerConfiguration);
    }


}