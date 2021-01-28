package com.batch.MultiDB.controller;

import java.util.Random;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.batch.MultiDB.model.mongo.StudentMongo;
import com.batch.MultiDB.repository.mongo.StudentMongoRepository;

@RestController
public class JobInvokerController{

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier("importStudentJob")
	Job importStudentJob;
	/*
	 * @Autowired
	 * 
	 * @Qualifier("inserttoMultipleClient") Job inserttoMultipleClient;
	 */
	@Autowired
	@Qualifier("importStudentMongoJob")
	Job importStudentMongoJob;
	
	@Autowired
	private StudentMongoRepository studentMongoRepository;

	@GetMapping("/runBatch")
	//@Scheduled(cron = "0 * 14 ? * *")
	@Scheduled(cron = "04 * * * * *")
	
	public BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException
	{
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("source", "Spring Boot")
				.addParameter("time", new JobParameter(System.currentTimeMillis()))
				.toJobParameters();
        JobExecution jobExecution = jobLauncher.run(importStudentJob, jobParameters);
        System.out.println("JobExecution: " + jobExecution.getStatus());
        System.out.println("Batch is Running...");
        while (jobExecution.isRunning()) {
            System.out.println("...");
        }
        return jobExecution.getStatus();
	}
	
	/*
	 * @GetMapping("/runBatchMultiple")
	 * 
	 * @Scheduled(cron = "0 * 13 ? * *") public BatchStatus inserttoMultipleClient()
	 * throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
	 * JobRestartException, JobInstanceAlreadyCompleteException { JobParameters
	 * jobParameters = new JobParametersBuilder() .addString("source",
	 * "Spring Boot") .addParameter("time", new
	 * JobParameter(System.currentTimeMillis())) .toJobParameters(); JobExecution
	 * jobExecution = jobLauncher.run(inserttoMultipleClient, jobParameters);
	 * System.out.println("JobExecution: " + jobExecution.getStatus());
	 * System.out.println("Batch is Running..."); while (jobExecution.isRunning()) {
	 * System.out.println("..."); } return jobExecution.getStatus(); }
	 */
	
	
	
	@GetMapping("/runMongo")
	//@Scheduled(cron = "0 * 14 ? * *")
	@Scheduled(cron = "04 * * * * *")
	
	public String runBatchMongo() {
		StudentMongo studentMongo=new  StudentMongo();
		Random rd= new Random();
		studentMongo.setId(rd.nextInt(1000));
		studentMongo.setStudentId(rd.nextInt(1000));
		studentMongo.setStudentName("Abdul");
		studentMongoRepository.insert(studentMongo);
				return "Sucessfully added record in mongo DB";
	}
	
	@GetMapping("/runBatchMongo")
	//@Scheduled(cron = "0 * 15 ? * *")
	@Scheduled(cron = "05 * * * * *")
	
	public BatchStatus inserttoMongo() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException
	{
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("source", "Spring Boot")
				.addParameter("time", new JobParameter(System.currentTimeMillis()))
				.toJobParameters();
        JobExecution jobExecution = jobLauncher.run(importStudentMongoJob, jobParameters);
        System.out.println("JobExecution: " + jobExecution.getStatus());
        System.out.println("Batch is Running...");
        while (jobExecution.isRunning()) {
            System.out.println("...");
        }
        return jobExecution.getStatus();
	}
}
	

