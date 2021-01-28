package com.batch.MultiDB.repository.client;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.batch.MultiDB.model.client.BaseClientStudent;

@NoRepositoryBean
public interface GenericRepository <T extends BaseClientStudent,ID extends Serializable> extends JpaRepository<T, ID>{
	
}
