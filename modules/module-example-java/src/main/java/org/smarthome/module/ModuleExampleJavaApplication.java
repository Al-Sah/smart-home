package org.smarthome.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ModuleExampleJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleExampleJavaApplication.class, args);
    }

    @Bean
    TopicsProvider topics() {
        return new TopicsProvider(new String[] { "hubs-messages"});
    }


}
