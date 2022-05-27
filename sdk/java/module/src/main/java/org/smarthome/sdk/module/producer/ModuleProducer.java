package org.smarthome.sdk.module.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.smarthome.sdk.models.Command;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class ModuleProducer {

    private final KafkaTemplate<String, Command> template;
    private final ProducerProvider provider;

    public ModuleProducer(KafkaTemplate<String, Command> template, ProducerProvider provider) {
        this.template = template;
        this.provider = provider;
    }


    public void sendAsync(final Command command) {
        sendAsync(command, new CommandSendCallback());
    }

    public void sendAsync(final Command command, KafkaSendCallback<String, Command> callback) {

        var future = template.send(createRecord(provider, command));
        future.addCallback(callback);
    }


    public ProducerRecord<String, Command> sendSync(final Command command) throws RuntimeException {
        try {
            return template
                    .send(createRecord(provider, command))
                    .get(20, TimeUnit.SECONDS)
                    .getProducerRecord();
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private static ProducerRecord<String, Command> createRecord(ProducerProvider provider, Command command){
        return new ProducerRecord<>(
                provider.getTopic(),
                provider.getPartition(),
                new Date().getTime(),
                provider.getKey(),
                command
        );
    }
}
