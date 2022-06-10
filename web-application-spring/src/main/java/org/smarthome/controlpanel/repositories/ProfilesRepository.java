package org.smarthome.controlpanel.repositories;

import org.smarthome.controlpanel.entities.ProfileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilesRepository extends CrudRepository<ProfileInfo, Integer> {
}
