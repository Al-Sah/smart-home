package org.smarthome.sdk.module.producer.config;


import org.smarthome.sdk.module.producer.ProducerProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultProducerProviderFactory {

    private final ModuleProducerConfiguration configuration;

    public DefaultProducerProviderFactory(ModuleProducerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Bean
    @ConditionalOnMissingBean(ProducerProvider.class)
    public ProducerProvider producerProvider(){
        return new ProducerProvider(configuration.getTopic(), configuration.getKey(), configuration.getPartition());
    }
}
