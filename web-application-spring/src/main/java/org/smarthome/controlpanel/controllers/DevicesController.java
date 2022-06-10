package org.smarthome.controlpanel.controllers;

import org.smarthome.controlpanel.dtos.DeviceAliasDTO;
import org.smarthome.controlpanel.models.DeviceAliasRequest;
import org.smarthome.controlpanel.services.AliasNotFoundException;
import org.smarthome.controlpanel.services.AliasesManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "alias")
public class DevicesController {

    private final AliasesManager aliasesManager;

    public DevicesController(AliasesManager aliasesManager) {
        this.aliasesManager = aliasesManager;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getDeviceAlias(Authentication user, @PathVariable String id)  {
        try {
            return new ResponseEntity<>(aliasesManager.getDeviceAlias(user.getName(), id), HttpStatus.OK);
        } catch (UsernameNotFoundException | AliasNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addDeviceAlias(Authentication user, @RequestBody DeviceAliasRequest deviceAlias)  {
        try {
            return new ResponseEntity<>(aliasesManager.addDeviceAlias(user.getName(), deviceAlias), HttpStatus.OK);
        } catch (UsernameNotFoundException | AliasNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/")
    public ResponseEntity<?> updateDeviceAlias(Authentication user, @RequestBody DeviceAliasRequest deviceAlias)  {
        try {
            return new ResponseEntity<>(aliasesManager.updateDeviceAlias(user.getName(), deviceAlias),HttpStatus.OK);
        } catch (UsernameNotFoundException | AliasNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/rm/{id}")
    public ResponseEntity<?> deleteDeviceAlias(Authentication user, @PathVariable String id)  {
        try {
            aliasesManager.deleteDeviceAlias(user.getName(), id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UsernameNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<DeviceAliasDTO>> getAllAliases(Authentication user)  {
        try {
            return new ResponseEntity<>(aliasesManager.getAllAliasesOfUser(user.getName()), HttpStatus.OK);
        } catch (UsernameNotFoundException | AliasNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
