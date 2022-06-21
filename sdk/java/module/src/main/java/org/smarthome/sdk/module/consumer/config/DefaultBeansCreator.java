package org.smarthome.sdk.module.consumer.config;


import org.smarthome.sdk.module.consumer.ConsumerTopicsProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultBeansCreator {

    private final ModuleConsumerConfiguration configuration;

    public DefaultBeansCreator(ModuleConsumerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Bean
    @ConditionalOnMissingBean(ConsumerTopicsProvider.class)
    public ConsumerTopicsProvider topicsProvider(){
        return new ConsumerTopicsProvider(configuration.getTopics());
    }
}
