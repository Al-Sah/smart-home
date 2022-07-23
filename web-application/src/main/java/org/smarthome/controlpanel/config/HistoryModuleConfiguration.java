package org.smarthome.controlpanel.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "module.history")
public class HistoryModuleConfiguration {

    private final String protocol;
    private final String host;
    private final String port;

    public HistoryModuleConfiguration(
            @DefaultValue("http") String protocol,
            @DefaultValue("127.0.0.1") String host,
            @DefaultValue("9090") String port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public String getUri(){
        return protocol + "://" + host + ':' + port;
    }
}
