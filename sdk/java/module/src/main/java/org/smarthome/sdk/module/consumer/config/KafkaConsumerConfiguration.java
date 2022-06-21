package org.smarthome.sdk.module.consumer.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.smarthome.sdk.models.HubMessage;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

    /**
     * properties from properties (application.yml) file
     */
    private final KafkaProperties properties;

    public KafkaConsumerConfiguration(KafkaProperties properties) {
        this.properties = properties;
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, HubMessage<?>>> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, HubMessage<?>>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    /**
     * Creates custom ConsumerFactory with specified properties
     */
    @Bean
    public ConsumerFactory<String, HubMessage<?>> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                properties.buildConsumerProperties(),
                new StringDeserializer(),
                new JsonDeserializer<>(HubMessage.class)
        );
    }
}
