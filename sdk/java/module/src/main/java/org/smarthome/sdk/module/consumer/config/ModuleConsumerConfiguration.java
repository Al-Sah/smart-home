package org.smarthome.sdk.module.consumer.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "consumer")
public class ModuleConsumerConfiguration {

    private final String[] topics;

    public ModuleConsumerConfiguration(String[] topics) {
        this.topics = topics;
    }
}

