package org.smarthome.sdk.hub.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.HubMessage;
import org.smarthome.sdk.models.json.JsonHubMessage;

/**
 * Class {@code HubMessageSerializer} is custom json serializer used by {@link org.apache.kafka.clients.producer.KafkaProducer}
 *
 * @see HubMessage
 * @author  Al-Sah
 */
public class HubMessageSerializer implements Serializer<JsonHubMessage> {

    private static final Logger logger = LoggerFactory.getLogger(HubMessageSerializer.class);

    /**
     * ObjectMapper must be configured to parse just not null fields
     */
    private final ObjectMapper mapper;

    public HubMessageSerializer() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public byte[] serialize(String topic, JsonHubMessage data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new SerializationException("Failed do serialize hub message");
        }
    }

}
