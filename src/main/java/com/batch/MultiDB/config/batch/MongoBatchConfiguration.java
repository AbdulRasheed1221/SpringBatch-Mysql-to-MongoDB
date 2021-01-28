package com.batch.MultiDB.config.batch;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.batch.MultiDB.batch.JobCompletionNotificationListener;
import com.batch.MultiDB.batch.MongoItemProcessor;
import com.batch.MultiDB.model.mongo.StudentMongo;
import com.batch.MultiDB.model.user.Student;

@Configuration
@EnableBatchProcessing
public class MongoBatchConfiguration
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
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Bean
	public Job importStudentMongoJob(JobCompletionNotificationListener listener)
	{
		return jobBuilderFactory.get("importStudentMongoJob").incrementer(new RunIdIncrementer())
				.listener(listener).flow(stepMongo()).end().build();
	}

	@Bean
	public Step stepMongo()
	{
		return stepBuilderFactory.get("stepMongo")
				.transactionManager(jpaTransactionManager)
				.<Student, StudentMongo>chunk(10)
				.reader(readerMongo())
				.processor(processorMongo())
				.writer(writerMongo())
				.build();
	}
	
	@Bean
	public JpaCursorItemReader<Student> readerMongo()
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
	public MongoItemProcessor processorMongo()
	{
		System.out.println("-----------Inside  processor() method--------");
		return new MongoItemProcessor();
	}

	/**
	 * The writer() method is used to write a data into the SQL.
	 */
	@Bean
	public MongoItemWriter<StudentMongo> writerMongo()
	{
		System.out.println("-----------Inside writer() method--------");
		MongoItemWriter<StudentMongo> writer = new MongoItemWriter<StudentMongo>();
		writer.setCollection("StudentMongo");
		writer.setTemplate(mongoTemplate);
		return writer;
	}

	
}
