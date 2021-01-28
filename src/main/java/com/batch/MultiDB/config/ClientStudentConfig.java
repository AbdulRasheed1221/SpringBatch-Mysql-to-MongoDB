package com.batch.MultiDB.config;


import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
		basePackages = {"com.batch.MultiDB.repository.client"},
		entityManagerFactoryRef = "clientStudentEntityManagerFactory",
		transactionManagerRef = "clientStudentTransactionManager"
		)
public class ClientStudentConfig {
	@Autowired 
	private Environment env;

	@Bean
	@ConfigurationProperties(prefix="spring.client.student") 
	public DataSourceProperties clientStudentDataSourceProperties() { 
		return new DataSourceProperties(); 
	}

	@Bean(name = "clientStudentDataSource")
	public DataSource clientStudentDataSource() { 
		DataSourceProperties properties = clientStudentDataSourceProperties(); 
		return DataSourceBuilder.create()
				.driverClassName(properties.getDriverClassName())
				.url(properties.getUrl())
				.username(properties.getUsername())
				.password(properties.getPassword()) .build(); 
	}

	@Bean(name="clientStudentEntityManagerFactory") 
	public LocalContainerEntityManagerFactoryBean clientStudentEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(clientStudentDataSource()); 
		factory.setPackagesToScan("com.batch.MultiDB.model.client");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setPersistenceUnitName("ClientStudent");

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
		jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		factory.setJpaProperties(jpaProperties);

		return factory;

	}
	
	@Bean(name="clientStudentTransactionManager") 
	public PlatformTransactionManager clientStudentTransactionManager(@Qualifier("clientStudentEntityManagerFactory") EntityManagerFactory emf)
	{
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean 
	public DataSourceInitializer clientStudentDataSourceInitializer() { 
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer(); 
		dataSourceInitializer.setDataSource(clientStudentDataSource());
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(); 
		databasePopulator.addScript(new	ClassPathResource("client-student-data.sql"));
		dataSourceInitializer.setDatabasePopulator(databasePopulator);
		dataSourceInitializer.setEnabled(env.getProperty("spring.client.student.initialize", Boolean.class, false)); 
		return dataSourceInitializer; 
	}

}
