package org.smarthome.sdk.module.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.models.json.CommandMapper;
import org.smarthome.sdk.models.json.JsonCommand;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class ModuleProducer {

    private final KafkaTemplate<String, JsonCommand> template;

    private final ProducerProvider provider;

    public ModuleProducer(KafkaTemplate<String, JsonCommand> template, ProducerProvider provider) {
        this.template = template;
        this.provider = provider;
    }


    public void sendAsync(final Command command) {
        sendAsync(command, new CommandSendCallback());
    }

    public void sendAsync(final Command data, KafkaSendCallback<String, JsonCommand> callback) {
        ListenableFuture<SendResult<String, JsonCommand>> future = template.send(
                provider.getTopic(),
                provider.getPartition(),
                new Date().getTime(),
                provider.getKey(),
                CommandMapper.getCommand(data)
        );
        future.addCallback(callback);
    }


    public ProducerRecord<String, JsonCommand> sendSync(final Command data) throws RuntimeException {
        try {
            return template.send(
                    provider.getTopic(),
                    provider.getPartition(),
                    new Date().getTime(),
                    provider.getKey(),
                    CommandMapper.getCommand(data)
            ).get(10, TimeUnit.SECONDS).getProducerRecord();
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
