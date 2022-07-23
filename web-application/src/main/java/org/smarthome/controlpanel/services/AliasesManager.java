package org.smarthome.controlpanel.services;

import org.smarthome.controlpanel.models.DeviceAliasRequest;
import org.smarthome.controlpanel.dtos.DeviceAliasDTO;
import org.smarthome.controlpanel.entities.DeviceAlias;
import org.smarthome.controlpanel.repositories.AliasesRepository;
import org.smarthome.controlpanel.repositories.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AliasesManager {

    private final AliasesRepository aliasesRepository;
    private final UsersRepository usersRepository;


    public AliasesManager(AliasesRepository aliasesRepository, UsersRepository usersRepository) {
        this.aliasesRepository = aliasesRepository;
        this.usersRepository = usersRepository;
    }

    public List<DeviceAliasDTO> getAllAliasesOfUser(String username){
        var user = usersRepository.findByLogin(username)
                .orElseThrow(()-> new UserNotFoundException(username));

        return user.getAliases().stream().map(als -> new DeviceAliasDTO(als, username)).collect(Collectors.toList());
    }

    public String getDeviceAlias(String user, String device){

        var alias = aliasesRepository.findByUserAndDevice(
                usersRepository.findByLogin(user).orElseThrow(()-> new UserNotFoundException(user)).getId(), device
        ).orElseThrow(()-> new AliasNotFoundException(user, device));

        return alias.getName();
    }

    @Transactional
    public void deleteDeviceAlias(String user, String device) {
        aliasesRepository.deleteByUserAndDevice(
                usersRepository.findByLogin(user).orElseThrow(()-> new UserNotFoundException(user)).getId(), device
        );
    }

    public DeviceAliasDTO addDeviceAlias(String user, DeviceAliasRequest deviceAlias) {
        var userId = usersRepository.findByLogin(user)
                .orElseThrow(()-> new UserNotFoundException(user)).getId();

        try {
            var res = aliasesRepository.save(new DeviceAlias(userId, deviceAlias.device(), deviceAlias.alias()));
            return new DeviceAliasDTO(res, user);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public DeviceAliasDTO updateDeviceAlias(String user, DeviceAliasRequest deviceAlias) {
        var userId = usersRepository.findByLogin(user)
                .orElseThrow(()-> new UserNotFoundException(user)).getId();

        var alias = aliasesRepository.findByUserAndDevice(userId, deviceAlias.device())
                .orElse(new DeviceAlias(userId, deviceAlias.device(), deviceAlias.alias()));
        alias.setDevice(deviceAlias.device());
        alias.setName(deviceAlias.alias());
        aliasesRepository.save(alias);

        return new DeviceAliasDTO(alias, user);
    }
}
