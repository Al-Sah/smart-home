package org.smarthome.laststate;

import org.smarthome.sdk.module.consumer.config.ModuleConsumerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {
        "org.smarthome.sdk.module.consumer",
        "org.smarthome.sdk.models",
        "org.smarthome.laststate"
})
@EnableConfigurationProperties(ModuleConsumerConfiguration.class)
public class LastStateModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(LastStateModuleApplication.class, args);
    }
}
