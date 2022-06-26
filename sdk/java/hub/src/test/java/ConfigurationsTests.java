import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.smarthome.sdk.hub.consumer.HubConsumerConfiguration;
import org.smarthome.sdk.hub.producer.HubProducerConfiguration;

import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class ConfigurationsTests {

    @Test
    public void testCorrectProducerConfigCreation(){
        var properties = new Properties();
        properties.put("test-property", "test-value");
        Assertions.assertDoesNotThrow(() -> new HubProducerConfiguration(
                "test-topic", "test-hub-id", properties, "test-hub-name")
        );
    }

    @Test
    public void testCorrectProducerConfigCreationFullForm(){
        var properties = new Properties();
        properties.put("test-property", "test-value");
        Assertions.assertDoesNotThrow(() -> new HubProducerConfiguration(
                "test-topic", "test-hub-id", properties, 1, TimeUnit.SECONDS,  null, null, "test-hub-name")
        );
    }

    @Test
    public void testIncorrectProducerConfigCreation(){
        var result = Assertions.assertThrows(IllegalArgumentException.class, () -> new HubProducerConfiguration(
                "    ", null, new Properties(), "test-hub-name")
        ).getMessage();
        System.out.println(result);
    }


    @Test
    public void testCorrectConsumerConfigCreation(){
        var properties = new Properties();
        properties.put("test-property", "test-value");
        Assertions.assertDoesNotThrow(() -> new HubConsumerConfiguration(
                        "test-topic", "test-hub-id", properties, command -> {})
        );
    }

    @Test
    public void testIncorrectConsumerConfigCreation(){
        var result = Assertions.assertThrows(IllegalArgumentException.class, () -> new HubConsumerConfiguration(
                "  ", null, new Properties(), command -> {})
        ).getMessage();
        System.out.println(result);
    }
}
