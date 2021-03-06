package org.smarthome.sdk.module.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.Command;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.support.SendResult;

public class CommandSendCallback implements KafkaSendCallback<String, Command> {


    private static final Logger logger = LoggerFactory.getLogger(CommandSendCallback.class);

    @Override
    public void onFailure(KafkaProducerException ex) {
        logger.error(ex.getMessage());
    }

    @Override
    public void onSuccess(SendResult<String, Command> result) {
        if(result == null){
            logger.info("message sent, but result is null");
        }else {
            logger.info(result.toString());
        }

    }
}
