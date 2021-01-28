package com.batch.MultiDB.batch;

import org.springframework.batch.item.ItemProcessor;

import com.batch.MultiDB.model.client.ClientStudent;
import com.batch.MultiDB.model.user.Student;

/**
 * Intermediate processor to do the operations after the reading the data from the CSV file and
 * before writing the data into SQL.
 */
public class StudentItemProcessor implements ItemProcessor<Student, ClientStudent>
{

	@Override
	public ClientStudent process(final Student student) throws Exception
	{
		System.out.println("-----------Inside process(final Student student) method--------");
		ClientStudent clientStudent=convert(student);
		System.out.println("Converting (" + student + ") into (" + clientStudent + ")");
		return clientStudent;
	}
	
	public ClientStudent convert(Student student) {
		ClientStudent clientStudent= new ClientStudent();
		clientStudent.setId(student.getId());
		clientStudent.setStudentName(student.getStudentName());
		clientStudent.setStudentId(student.getStudentId());
		clientStudent.setPhoneNumber(student.getPhoneNumber());
		clientStudent.setAge(student.getAge());
		clientStudent.setLocation(student.getLocation());
		return clientStudent;
	}
}
