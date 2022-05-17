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
    ListenerProvider listenerProvider() {
        return new ListenerProvider(new String[] { "hubs-messages"}, "some-id", "my-group");
    }

    /*
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    ModuleConsumer moduleConsumer() {
        return new ModuleConsumer();
    }*/

}
