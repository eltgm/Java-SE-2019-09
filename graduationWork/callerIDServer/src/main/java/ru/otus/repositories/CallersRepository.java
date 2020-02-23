package ru.otus.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.Caller;

import java.util.Optional;

public interface CallersRepository extends MongoRepository<Caller, String> {
    Optional<Caller> findByTelephoneNumber(String telephoneNumber);
}
