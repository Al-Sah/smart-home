package org.smarthome.controlpanel;

import org.smarthome.controlpanel.config.HistoryModuleConfiguration;
import org.smarthome.sdk.module.producer.ProducerProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {
        "org.smarthome.sdk.module.producer",
        "org.smarthome.sdk.models",
        "org.smarthome.controlpanel"
})
@EnableConfigurationProperties(HistoryModuleConfiguration.class)
public class ControlPanelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControlPanelApplication.class, args);
    }

    @Bean
    public ProducerProvider getProducerProvider(){
        return new ProducerProvider("modules-messages", null, 0);
    }
}
