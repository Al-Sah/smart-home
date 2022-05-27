package org.smarthome.climate;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.smarthome.sdk.hub.device.ConstantDeviceProperty;
import org.smarthome.sdk.hub.device.DeviceComponent;
import org.smarthome.sdk.hub.device.IntegerNumberConstraint;
import org.smarthome.sdk.hub.device.WritableDeviceProperty;
import org.smarthome.sdk.hub.producer.DeviceMessageCallback;
import org.smarthome.sdk.hub.producer.HubConfiguration;
import org.smarthome.sdk.hub.producer.HubProducer;
import org.smarthome.sdk.hub.producer.HubProducerException;
import org.smarthome.sdk.models.DeviceDisconnectionDetails;
import org.smarthome.sdk.models.DeviceType;
import org.smarthome.sdk.models.HubShutdownDetails;
import org.smarthome.sdk.models.MessageAction;

import java.io.IOException;
import java.util.Properties;

public class Main {

    public static String hubId = "smart-home-climate-hub";
    public static String topic = "hubs-messages";
    public static HubProducer producer;

    public static void main(String[] args) {

        try {
            var properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            var producerConfiguration = new HubConfiguration(topic, hubId, properties, "climate-hub");
            producer = new HubProducer(producerConfiguration);
        } catch (HubProducerException e) {
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


}