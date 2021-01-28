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
import org.springframework.context.annotation.Primary;
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
		basePackages = {"com.batch.MultiDB.repository.user"},
		entityManagerFactoryRef = "StudentEntityManagerFactory",
		transactionManagerRef = "StudentTransactionManager"
		)
public class StudentConfig {
	@Autowired 
	private Environment env;

	@Primary
	@Bean
	@ConfigurationProperties(prefix="spring.user.student") 
	public DataSourceProperties studentDataSourceProperties() { 
		return new DataSourceProperties(); 
	}

	@Primary
	@Bean(name = "StudentDataSource")
	public DataSource studentDataSource() { 
		DataSourceProperties properties = studentDataSourceProperties(); 
		return DataSourceBuilder.create()
				.driverClassName(properties.getDriverClassName())
				.url(properties.getUrl())
				.username(properties.getUsername())
				.password(properties.getPassword()) .build(); 
	}

	@Primary
	@Bean(name="StudentEntityManagerFactory") 
	public LocalContainerEntityManagerFactoryBean studentEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(studentDataSource()); 
		factory.setPackagesToScan("com.batch.MultiDB.model.user");
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setPersistenceUnitName("Student");

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
		jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
		factory.setJpaProperties(jpaProperties);

		return factory;

	}
	
	@Primary
	@Bean(name="StudentTransactionManager") 
	public PlatformTransactionManager employeeTransactionManager(@Qualifier("StudentEntityManagerFactory") EntityManagerFactory emf)
	{
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean 
	public DataSourceInitializer studentDataSourceInitializer() { 
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer(); 
		dataSourceInitializer.setDataSource(studentDataSource());
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(); 
		databasePopulator.addScript(new	ClassPathResource("student-data.sql"));
		dataSourceInitializer.setDatabasePopulator(databasePopulator);
		dataSourceInitializer.setEnabled(env.getProperty("spring.user.student.initialize", Boolean.class, false)); 
		return dataSourceInitializer; 
	}

}
