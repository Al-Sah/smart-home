package org.smarthome.laststate.repositories;

import org.smarthome.laststate.entities.HubState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubStateRepository extends MongoRepository<HubState, String> {}
