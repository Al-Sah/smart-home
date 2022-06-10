package org.smarthome.controlpanel.repositories;

import org.smarthome.controlpanel.entities.UserCredentials;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsRepository extends CrudRepository<UserCredentials, Integer> {

    Optional<UserCredentials> findByLogin(String login);
}