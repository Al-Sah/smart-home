package org.smarthome.laststate.repositories;

import org.smarthome.laststate.entities.HubStateDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubStateDetailsRepository extends MongoRepository<HubStateDetails, String> {}
