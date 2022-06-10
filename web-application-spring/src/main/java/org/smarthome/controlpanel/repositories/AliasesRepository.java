package org.smarthome.controlpanel.repositories;

import org.smarthome.controlpanel.entities.DeviceAlias;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AliasesRepository extends CrudRepository<DeviceAlias, Integer> {
}