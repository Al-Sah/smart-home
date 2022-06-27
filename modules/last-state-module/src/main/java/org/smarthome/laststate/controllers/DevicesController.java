package org.smarthome.laststate.controllers;

import org.smarthome.laststate.models.FullDeviceDescription;
import org.smarthome.laststate.services.DataBaseManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DevicesController {

    private final DataBaseManager dataBaseManager;

    public DevicesController(DataBaseManager dataBaseManager) {
        this.dataBaseManager = dataBaseManager;
    }

    @GetMapping("/device")
    public FullDeviceDescription getDevice(@RequestParam String id){
        return dataBaseManager.getDevice(id);
        // TODO handle exception
    }
}
