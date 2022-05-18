package org.smarthome.module;

import org.smarthome.sdk.module.TopicsProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(scanBasePackages = {"org.smarthome.sdk.module"})
public class ModuleExampleJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleExampleJavaApplication.class, args);
    }

    @Bean
    TopicsProvider topics() {
        return new TopicsProvider(new String[] { "hubs-messages"});
    }


}
