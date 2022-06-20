package org.smarthome.controlpanel.controllers;


import lombok.NonNull;
import org.smarthome.sdk.models.Command;
import org.smarthome.sdk.module.producer.ModuleProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommandsController {

    private final ModuleProducer moduleProducer;

    public CommandsController(ModuleProducer moduleProducer) {
        this.moduleProducer = moduleProducer;
    }


    @PostMapping("/command")
    public ResponseEntity<?> getDeviceAlias(@NonNull @RequestBody Command command)  {
        try {
            return new ResponseEntity<>(moduleProducer.sendSync(command).timestamp().toString(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
