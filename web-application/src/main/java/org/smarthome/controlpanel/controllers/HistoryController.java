package org.smarthome.controlpanel.controllers;

import org.smarthome.controlpanel.models.HistoryRequest;
import org.smarthome.controlpanel.services.HistoryProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistoryController {

    private final HistoryProvider historyProvider;

    public HistoryController(HistoryProvider historyProvider) {
        this.historyProvider = historyProvider;
    }


    @GetMapping("/history")
    public ResponseEntity<Object[]> getDeviceHistory(HistoryRequest request)  {
        return new ResponseEntity<>(historyProvider.getDeviceHistory(request), HttpStatus.OK);
    }


}
