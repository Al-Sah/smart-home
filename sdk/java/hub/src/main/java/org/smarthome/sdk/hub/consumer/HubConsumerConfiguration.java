package org.smarthome.sdk.hub.consumer;

import java.util.Properties;

public class HubConsumerConfiguration {

    private final String topic;
    private final String hubId;
    private final Properties properties;
    private final CommandsHandler commandsHandler;

    public HubConsumerConfiguration(
            String topic,
            String hubId,
            Properties properties,
            CommandsHandler commandsHandler) throws IllegalArgumentException {

        this.properties = properties;
        this.topic = topic;
        this.hubId = hubId;
        this.commandsHandler = commandsHandler;

        validate();
    }

    private void validate(){
        var sb = new StringBuilder();

        if(properties == null || properties.isEmpty()){
            sb.append("\nfield 'properties' is null or empty");
        }
        if(topic == null || topic.isBlank()){
            sb.append("\nfield 'topic' is null or blank");
        }
        if(hubId == null || hubId.isBlank()){
            sb.append("\nfield 'hub-id' is null or blank");
        }
        if(commandsHandler == null){
            sb.append("\nfield 'commandsHandler' is null");
        }
        var result = sb.toString();
        if(!result.isEmpty()){
            throw new IllegalArgumentException("invalid configuration; errors: \n" + result);
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getTopic() {
        return topic;
    }

    public String getHubId() {
        return hubId;
    }

    public CommandsHandler getCommandsHandler() {
        return commandsHandler;
    }
}

