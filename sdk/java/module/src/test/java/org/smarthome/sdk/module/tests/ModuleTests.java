package org.smarthome.sdk.module.tests;

import org.junit.jupiter.api.Test;
import org.smarthome.sdk.module.consumer.ModuleConsumer;
import org.smarthome.sdk.module.consumer.config.ModuleConsumerConfiguration;
import org.smarthome.sdk.module.producer.ModuleProducer;
import org.smarthome.sdk.module.producer.config.ModuleProducerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest(classes = ApplicationTestContext.class)
@EnableConfigurationProperties({
        ModuleConsumerConfiguration.class,
        ModuleProducerConfiguration.class
})

@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9088", "port=9088" })
class ModuleTests {

    @Autowired
    private ModuleProducer moduleProducer;

    @Autowired
    private ModuleConsumer moduleConsumer;


    @Test
    public void contextLoaded() {
    }

}
