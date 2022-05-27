package org.smarthome.sdk.hub.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.Command;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class HubConsumer {

    private final HubConsumerConfiguration configuration;
    private final KafkaConsumer<String, Command> consumer;

    private final Thread listener;
    private boolean isRunning = true;

    private static final Logger logger = LoggerFactory.getLogger(HubConsumer.class);


    public HubConsumer(HubConsumerConfiguration configuration) throws HubConsumerException {
        this.configuration = configuration;

        this.consumer = new KafkaConsumer<>(
                configuration.getProperties(),
                new StringDeserializer(),
                new CommandDeserializer()
        );

        try {
            consumer.subscribe(List.of(configuration.getTopic()));
        } catch (Exception e) {
            throw new HubConsumerException(e.getMessage());
        }

        listener = new Thread(this::readRecordsInWhileLoop);
        listener.start();
    }



    private void readRecordsInWhileLoop(){
        while (isRunning) {
            try {
                ConsumerRecords<String, Command> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Command> record : records) {
                    if(!Objects.equals(configuration.getHubId(), record.value().getHub())){
                        continue;
                    }
                    configuration.getCommandsHandler().handleCommand(record.value());
                }
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
    }

    public void stop(){

        // prevent
        if(!isRunning){
            return;
        }
        isRunning = false;
        try {
            consumer.close();
            listener.join(5000);
        } catch (InterruptedException ignored) {
        }
    }


}
