package org.smarthome.laststate.repositories;

import org.smarthome.sdk.models.DeviceMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevicesRepository extends MongoRepository<DeviceMetadata, String> {}
