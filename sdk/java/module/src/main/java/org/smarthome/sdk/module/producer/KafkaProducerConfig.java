package org.smarthome.sdk.module.producer;

import org.apache.kafka.common.serialization.StringSerializer;
import org.smarthome.sdk.models.Command;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class KafkaProducerConfig {


    /**
     * properties from properties (application.yml) file
     */
    private final KafkaProperties properties;

    public KafkaProducerConfig(KafkaProperties properties) {
        this.properties = properties;
    }


    @Bean
    public ProducerFactory<String, Command> producerFactory() {
        return new DefaultKafkaProducerFactory<>(
                properties.buildProducerProperties(),
                new StringSerializer(),
                new JsonSerializer<>()
        );
    }

    @Bean
    public KafkaTemplate<String, Command> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
