package com.batch.MultiDB.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch.MultiDB.model.user.Student;

@Repository
public interface StudentRepository extends JpaRepository <Student, Integer>{
	
	
}

