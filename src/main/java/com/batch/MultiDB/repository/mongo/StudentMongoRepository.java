package com.batch.MultiDB.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.batch.MultiDB.model.mongo.StudentMongo;

@Repository
public interface StudentMongoRepository extends MongoRepository<StudentMongo, Integer>{

}
