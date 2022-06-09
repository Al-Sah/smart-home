package org.smarthome.controlpanel;

import org.smarthome.controlpanel.config.HistoryModuleConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(HistoryModuleConfiguration.class)
public class ControlPanelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControlPanelApplication.class, args);
    }

}
