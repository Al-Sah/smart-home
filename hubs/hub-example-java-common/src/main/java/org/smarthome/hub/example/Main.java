package org.smarthome.hub.example;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.hub.HubProducerException;
import org.smarthome.sdk.models.DeviceData;
import org.smarthome.sdk.models.HubMessage;

import org.smarthome.sdk.hub.HubProducer;
import org.smarthome.sdk.models.ProducerConfiguration;

import java.util.*;

public class Main {

    public static HubProducer producer;
    public static String habId = "java-hub-sample-use-sdk";
    public static String topic = "hubs-messages";

    // TODO logger config
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {

        var sensor1 = new SensorImitator(UUID.randomUUID().toString(), "thermometer", "temperature imitator1");
        var sensor2 = new SensorImitator(UUID.randomUUID().toString(), "thermometer", "temperature imitator2");


        try {
            var data = new ArrayList<>(List.of(new DeviceData(habId, "hub", "test-hub")));
            setupProducer(new HubMessage(HubMessage.Action.HUB_START, data));
        } catch (HubProducerException e) {
            System.out.println(e.getMessage());
            return;
        }


        try {
            producer.send(
                    createDevicesConnectedMessage(List.of(sensor1, sensor2)),
                    (event, ex)-> System.out.println(event));
        } catch (HubProducerException e) {
            System.out.println(e.getMessage());
        }


        for (int i = 0; i < 10; i++) {
            try {
                producer.send(
                        createDevicesDataMessage(List.of(sensor1, sensor2)),
                        (event, ex)-> System.out.println(event));
                Thread.sleep(3000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


        try {
            producer.send(
                    createDevicesDisconnectedMessage(List.of(sensor1, sensor2)),
                    (event, ex)-> System.out.println(event));
        } catch (HubProducerException e) {
            System.out.println(e.getMessage());
        }


        try {
            producer.stop((event, ex)-> System.out.println(event));
        } catch (HubProducerException e) {
            throw new RuntimeException(e);
        }

    }

    public static HubMessage createDevicesConnectedMessage(List<SensorImitator> sensors){

        var data = new ArrayList<DeviceData>();
        for (SensorImitator sensor : sensors) {
            data.add(new DeviceData(sensor.getId(), sensor.getType(), sensor.getName()));
        }
        return new HubMessage(HubMessage.Action.DEVICES_CONNECTED, data);
    }

    public static HubMessage createDevicesDisconnectedMessage(List<SensorImitator> sensors){

        var data = new ArrayList<DeviceData>();
        for (SensorImitator sensor : sensors) {
            data.add(new DeviceData(sensor.getId(), "Connection lost"));
        }
        return new HubMessage(HubMessage.Action.DEVICES_DISCONNECTED, data);
    }

    public static HubMessage createDevicesDataMessage(List<SensorImitator> sensors){

        var data = new ArrayList<DeviceData>();
        for (SensorImitator sensor : sensors) {
            data.add(new DeviceData(sensor.getId(), sensor.getData()));
        }
        return new HubMessage(HubMessage.Action.DEVICE_MESSAGE, data);
    }

    public static void setupProducer(HubMessage initialMessage) throws HubProducerException {

        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        var producerConfiguration = new ProducerConfiguration(topic, habId, properties, 0, null);
        producer = new HubProducer(producerConfiguration, initialMessage, (event, ex) -> System.out.println(event));
    }

}