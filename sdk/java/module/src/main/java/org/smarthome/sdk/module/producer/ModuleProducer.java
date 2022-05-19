package org.smarthome.sdk.module.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.models.json.CommandMapper;
import org.smarthome.sdk.models.json.JsonCommand;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class ModuleProducer {

    private final KafkaTemplate<String, JsonCommand> template;

    private final static String HEADER_HUB_ID = "hub-id";
    private final ProducerProvider provider;

    public ModuleProducer(KafkaTemplate<String, JsonCommand> template, ProducerProvider provider) {
        this.template = template;
        this.provider = provider;
    }


    public void sendAsync(final Command command, final String hub) {
        sendAsync(command, hub, new CommandSendCallback());
    }

    public void sendAsync(final Command command, final String hubId, KafkaSendCallback<String, JsonCommand> callback) {

        var future = template.send(createRecord(provider, command, hubId));
        future.addCallback(callback);
    }


    public ProducerRecord<String, JsonCommand> sendSync(final Command command, final String hubId) throws RuntimeException {
        try {
            return template
                    .send(createRecord(provider, command, hubId))
                    .get(10, TimeUnit.SECONDS)
                    .getProducerRecord();
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static ProducerRecord<String, JsonCommand> createRecord(ProducerProvider provider, Command command, String hubId){
        return new ProducerRecord<>(
                provider.getTopic(),
                provider.getPartition(),
                new Date().getTime(),
                provider.getKey(),
                CommandMapper.getCommand(command),
                List.of(new RecordHeader(HEADER_HUB_ID, hubId.getBytes(StandardCharsets.UTF_8)))
        );
    }
}
