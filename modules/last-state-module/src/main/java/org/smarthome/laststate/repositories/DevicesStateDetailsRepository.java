package org.smarthome.laststate.repositories;

import org.smarthome.laststate.entities.DeviceStateDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DevicesStateDetailsRepository extends MongoRepository<DeviceStateDetails, String> {}