package com.batch.MultiDB.batch;

import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batch.MultiDB.model.client.ClientStudent;
import com.batch.MultiDB.repository.client.ClientStudentRepository;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport
{
	@Autowired
	private final ClientStudentRepository clientStudentRepository;

	@Autowired
	public JobCompletionNotificationListener(ClientStudentRepository clientStudentRepository)
	{
		this.clientStudentRepository = clientStudentRepository;
	}

	@Override
	public void afterJob(JobExecution jobExecution)
	{
		if (jobExecution.getStatus() == BatchStatus.COMPLETED)
		{
			System.out.println(" -------- JOB FINISHED ------------------ ");
			List<ClientStudent> studentList = clientStudentRepository.findAll();
			System.out.println("Found <" + studentList.size() + "> in the database.");
		}
	}
}
