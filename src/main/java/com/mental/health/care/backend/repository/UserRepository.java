package com.mental.health.care.backend.repository;
import com.mental.health.care.backend.model.BaseUser;
import com.mental.health.care.backend.model.Client;
import com.mental.health.care.backend.model.Psikiater;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<BaseUser, String> {
    Optional<BaseUser> findByEmail(String email);
    Optional<Client> findByUsername(String username);
    Optional<Psikiater> findByNoStr(String noStr);
}
