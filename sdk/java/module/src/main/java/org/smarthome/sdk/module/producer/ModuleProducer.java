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


    public void sendAsync(final UserCommand userCommand) {
        sendAsync(userCommand, new CommandSendCallback());
    }

    public void sendAsync(final UserCommand userCommand, KafkaSendCallback<String, Command> callback) {

        var future = template.send(createRecord(provider, userCommand));
        future.addCallback(callback);
    }


    public ProducerRecord<String, Command> sendSync(final UserCommand userCommand) throws RuntimeException {
        try {
            return template
                    .send(createRecord(provider, userCommand))
                    .get(20, TimeUnit.SECONDS)
                    .getProducerRecord();
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private static ProducerRecord<String, Command> createRecord(ProducerProvider provider, UserCommand userCommand){
        return new ProducerRecord<>(
                provider.getTopic(),
                provider.getPartition(),
                new Date().getTime(),
                provider.getKey(),
                new Command(null, null, null, null, null, 0));
    }
}
