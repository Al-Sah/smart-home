package org.smarthome.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.module.producer.ModuleProducer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final ModuleProducer moduleProducer;
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    public TestController(ModuleProducer moduleProducer) {
        this.moduleProducer = moduleProducer;
    }

    @PostMapping(path = "/send/{task}")
    public void sendCommand(@PathVariable String task) {
        logger.info("Received task: " + task);
        moduleProducer.sendSync(new Command("device1", task, Command.getFuture(30)), "hub1");
        // curl -X POST http://localhost:8080/send/data
    }
}
