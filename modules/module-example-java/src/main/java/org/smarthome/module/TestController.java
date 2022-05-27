package org.smarthome.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.module.producer.ModuleProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final ModuleProducer moduleProducer;
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    public TestController(ModuleProducer moduleProducer) {
        this.moduleProducer = moduleProducer;
    }

    @PostMapping(path = "/send")
    public void sendCommand(@RequestBody Command command) {
        logger.info("Received command: " + command.toString());
        moduleProducer.sendSync(command);
    }
}
