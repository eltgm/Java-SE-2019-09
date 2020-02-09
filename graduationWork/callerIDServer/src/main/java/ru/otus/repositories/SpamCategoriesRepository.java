package ru.otus.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.models.SpamType;

public interface SpamCategoriesRepository extends MongoRepository<SpamType, ObjectId> {
}
