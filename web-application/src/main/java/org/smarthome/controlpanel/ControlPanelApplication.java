package org.smarthome.controlpanel;

import org.smarthome.controlpanel.config.HistoryModuleConfiguration;
import org.smarthome.sdk.module.producer.config.ModuleProducerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {
        "org.smarthome.sdk.module.producer",
        "org.smarthome.sdk.models",
        "org.smarthome.controlpanel"
})
@EnableConfigurationProperties({HistoryModuleConfiguration.class, ModuleProducerConfiguration.class})
public class ControlPanelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControlPanelApplication.class, args);
    }
}
