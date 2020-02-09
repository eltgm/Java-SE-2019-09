package ru.otus.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.Caller;

import java.util.Optional;

public interface CallersRepository extends MongoRepository<Caller, ObjectId> {
    Optional<Caller> findByTelephoneNumber(String telephoneNumber);
}
