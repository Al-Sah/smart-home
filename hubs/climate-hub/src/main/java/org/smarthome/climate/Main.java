package org.smarthome.climate;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.smarthome.climate.metadata.MetadataFactory;
import org.smarthome.sdk.hub.HubProducer;
import org.smarthome.sdk.hub.HubProducerException;
import org.smarthome.sdk.models.DeviceData;
import org.smarthome.sdk.models.HubMessage;
import org.smarthome.sdk.models.MessageAction;
import org.smarthome.sdk.models.ProducerConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

    private static final String[] types = {"celsius", "fahrenheit"};

    private static final Sensor[] sensors = new Sensor[]{
            new SimpleSensor<Float>("86c4ed10-6ecc-41c7","simple-thermometer","celsius"),
            new SimpleSensor<Float>("39e0581f-c7ed-45ba","simple-thermometer","fahrenheit"),
            new SimpleSensor<Float>("e65079fa-104e-45fa","hygrometer", "absolute humidity"),
            new SimpleSensor<Float>("b853a877-6497-45c7","lux-meter", "lux"),

            new SwitchableSensor<Float>("8fcfe9bc-23b0-42b7","thermometer", types, "celsius"),

            new CombinedSensor( "b853a877-6497-45c7", "advanced-thermometer",
                    new ArrayList<>(List.of(
                            new SimpleSensor<Float>("child1", "simple-thermometer", "celsius"),
                            new SimpleSensor<Float>("child2", "simple-thermometer", "celsius"),
                            new SimpleSensor<Float>("child3", "lux-meter", "lux"))
                    )
            ),
            new CombinedSensor("b768c62d-14c3-4c53", "advanced-hygrometer",
                    new ArrayList<>(List.of(
                            new SimpleSensor<Float>("child1", "simple-thermometer", "celsius"),
                            new SimpleSensor<Float>("child2", "hygrometer", "absolute humidity"),
                            new SimpleSensor<Float>("child3", "relative-humidity-calculator", "relative humidity"))
                    )
            ),
            new CombinedSensor( "7a6d1020-245d-4999", "advanced-hygrometer",
                    new ArrayList<>(List.of(
                            new SimpleSensor<Float>("child1", "simple-thermometer", "celsius"),
                            new SimpleSensor<Float>("child2", "hygrometer", "absolute humidity"),
                            new SimpleSensor<Float>("child3", "relative-humidity-calculator", "relative humidity"))
                    )
            )
    };


    public static String hubId = "smart-home-climate-hub";
    public static String topic = "hubs-messages";

    public static HubProducer producer;



    public static void main(String[] args) {

        try {
            setupProducer(new HubMessage<>(MessageAction.HUB_START, "climate-hub"));
        } catch (HubProducerException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            producer.send( createDevicesConnectedMessage(), (event, ex)-> System.out.println(event));
        } catch (HubProducerException e) {
            System.out.println(e.getMessage());
        }




        try {
            System.out.println("Hit Enter to terminate...");
            System.in.read();
            producer.stop((event, ex)-> System.out.println(event), "user-requested-shutdown");
        } catch (HubProducerException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static void setupProducer(HubMessage<String> initialMessage) throws HubProducerException {

        var properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        var producerConfiguration = new ProducerConfiguration(topic, hubId, properties, 0, 600,null);
        producer = new HubProducer(producerConfiguration, initialMessage);
    }

    public static HubMessage<List<DeviceData>> createDevicesConnectedMessage(){

        var data = new ArrayList<DeviceData>();
        for (var sensor : sensors) {

            data.add(new DeviceData(sensor.getId(), sensor.getType(), "undefined", MetadataFactory.get(sensor).toString()));
        }
        return new HubMessage<>(MessageAction.DEVICES_CONNECTED, data);
    }

}