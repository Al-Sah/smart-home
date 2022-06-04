package org.smarthome.laststate.repositories;

import org.smarthome.laststate.entities.DeviceStateDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DevicesStateDetailsRepository extends MongoRepository<DeviceStateDetails, String> {

    List<DeviceStateDetails> findAllByOwnerAndActive(String owner, Boolean active);
    List<DeviceStateDetails> findAllByOwner(String owner);

}