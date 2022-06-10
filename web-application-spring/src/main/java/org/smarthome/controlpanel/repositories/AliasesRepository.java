package org.smarthome.controlpanel.repositories;

import org.smarthome.controlpanel.entities.DeviceAlias;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AliasesRepository extends CrudRepository<DeviceAlias, Integer> {

    Optional<DeviceAlias> findByUserAndDevice(Integer user, String deviceId);
    void deleteByUserAndDevice(Integer user, String deviceId);
}