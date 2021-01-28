package com.batch.MultiDB.repository.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.MultiDB.model.client.ClientStudent;

@Repository
public interface ClientStudentRepository extends JpaRepository<ClientStudent, Integer> {

}
