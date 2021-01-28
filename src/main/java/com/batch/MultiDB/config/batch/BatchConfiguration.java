package com.batch.MultiDB.config.batch;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.batch.MultiDB.batch.JobCompletionNotificationListener;
import com.batch.MultiDB.batch.StudentItemProcessor;
import com.batch.MultiDB.model.client.ClientStudent;
import com.batch.MultiDB.model.user.Student;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration
{
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	@Qualifier("StudentEntityManagerFactory") 
	EntityManagerFactory entityManagerFactory;
	
	@Autowired
	@Qualifier("clientStudentEntityManagerFactory") 
	EntityManagerFactory clientEntityManagerFactory;
	
	@Autowired
	@Qualifier("clientStudentTransactionManager")
	PlatformTransactionManager jpaTransactionManager;

	@Bean
	public Job importStudentJob(JobCompletionNotificationListener listener)
	{
		return jobBuilderFactory.get("importStudentJob").incrementer(new RunIdIncrementer())
				.listener(listener).flow(step()).end().build();
	}

	@Bean
	public Step step()
	{
		return stepBuilderFactory.get("step1")
				.transactionManager(jpaTransactionManager)
				.<Student, ClientStudent>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	@Bean
	public JpaCursorItemReader<Student> reader()
	{
		System.out.println("-----------Inside reader() method--------");
		JpaCursorItemReader<Student> reader = new JpaCursorItemReader<Student>();
		reader.setName("StudentReader");
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setQueryString("Select S from Student S");
		reader.setMaxItemCount(100);
		return reader;
	}

	/**
	 * Intermediate processor to do the operations after the reading the data from the CSV file and
	 * before writing the data into SQL.
	 */
	@Bean
	public StudentItemProcessor processor()
	{
		System.out.println("-----------Inside  processor() method--------");
		return new StudentItemProcessor();
	}

	/**
	 * The writer() method is used to write a data into the SQL.
	 */
	@Bean
	public JpaItemWriter<ClientStudent> writer()
	{
		System.out.println("-----------Inside writer() method--------");
		JpaItemWriter<ClientStudent> writer = new JpaItemWriter<ClientStudent>();
		writer.setEntityManagerFactory(clientEntityManagerFactory);
		return writer;
	}

	
}
