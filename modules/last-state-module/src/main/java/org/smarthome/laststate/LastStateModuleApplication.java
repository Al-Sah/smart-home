package org.smarthome.laststate;

import org.smarthome.sdk.module.consumer.ConsumerTopicsProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {
        "org.smarthome.sdk.module.consumer",
        "org.smarthome.sdk.models",
        "org.smarthome.laststate"
})
public class LastStateModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(LastStateModuleApplication.class, args);
    }

    @Bean
    ConsumerTopicsProvider listenerTopics() {
        return new ConsumerTopicsProvider(new String[] {"hubs-messages"});
    }


}
