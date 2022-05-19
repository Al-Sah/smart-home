package org.smarthome.module;

import org.smarthome.sdk.module.consumer.ConsumerTopicsProvider;
import org.smarthome.sdk.module.producer.ProducerProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(scanBasePackages = {"org.smarthome.sdk.module", "org.smarthome.module"})
public class ModuleExampleJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleExampleJavaApplication.class, args);
    }

    @Bean
    ConsumerTopicsProvider listenerTopics() {
        return new ConsumerTopicsProvider(new String[] {"hubs-messages"});
    }

    @Bean
    ProducerProvider producerProvider() {
        return new ProducerProvider("modules-messages", "", 0);
    }

}
