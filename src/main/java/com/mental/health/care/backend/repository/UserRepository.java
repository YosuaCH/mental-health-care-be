package com.mental.health.care.backend.repository;
import com.mental.health.care.backend.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Client, String> {
    Optional<Client> findByUsername(String username);
    Optional<Client> findByEmail(String email);
}
