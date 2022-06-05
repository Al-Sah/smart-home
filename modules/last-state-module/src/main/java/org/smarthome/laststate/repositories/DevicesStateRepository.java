package org.smarthome.laststate.repositories;

import org.smarthome.laststate.entities.DeviceState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DevicesStateRepository extends MongoRepository<DeviceState, String> {

    List<DeviceState> findAllByOwnerAndActive(String owner, Boolean active);
    List<DeviceState> findAllByOwner(String owner);

}