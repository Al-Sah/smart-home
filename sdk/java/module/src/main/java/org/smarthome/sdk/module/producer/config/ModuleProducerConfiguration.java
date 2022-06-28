package org.smarthome.sdk.module.producer.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.lang.Nullable;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "producer")
public class ModuleProducerConfiguration {

    @Nullable
    private final String key;
    private final String topic;
    private final Integer partition;

    public ModuleProducerConfiguration(@DefaultValue("0") Integer partition, String topic, @Nullable String key) {
        this.partition = partition;
        this.topic = topic;
        this.key = key;
    }


}

