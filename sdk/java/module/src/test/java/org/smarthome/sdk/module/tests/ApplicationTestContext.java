package org.smarthome.sdk.module.tests;

import org.smarthome.sdk.module.consumer.config.ModuleConsumerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication(scanBasePackages = {
        "org.smarthome.sdk.module",
        "org.smarthome.sdk.models",
})
@EnableConfigurationProperties(ModuleConsumerConfiguration.class)
public class ApplicationTestContext {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTestContext.class, args);
    }
}
