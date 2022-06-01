package org.smarthome.laststate.repositories;

import org.smarthome.sdk.models.DeviceMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevicesErrorsRepository extends MongoRepository<DeviceMessage, String> {}
