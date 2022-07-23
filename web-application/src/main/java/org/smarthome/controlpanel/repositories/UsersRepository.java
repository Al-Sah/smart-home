package org.smarthome.controlpanel.repositories;

import org.smarthome.controlpanel.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<User, Integer> {

    Optional<User> findByLogin(String login);
}