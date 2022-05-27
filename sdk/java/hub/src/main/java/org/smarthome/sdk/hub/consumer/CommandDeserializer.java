package org.smarthome.sdk.hub.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.Command;

public class CommandDeserializer implements Deserializer<Command> {

    private static final Logger logger = LoggerFactory.getLogger(CommandDeserializer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Command deserialize(String topic, byte[] bytes) {
        try {
            return mapper.readValue(bytes, Command.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
