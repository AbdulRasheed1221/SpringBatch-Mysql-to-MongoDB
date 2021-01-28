package com.batch.MultiDB.batch;

import org.springframework.batch.item.ItemProcessor;

import com.batch.MultiDB.model.mongo.StudentMongo;
import com.batch.MultiDB.model.user.Student;

/**
 * Intermediate processor to do the operations after the reading the data from the CSV file and
 * before writing the data into SQL.
 */
public class MongoItemProcessor implements ItemProcessor<Student, StudentMongo>
{

	@Override
	public StudentMongo process(final Student student) throws Exception
	{
		System.out.println("-----------Inside process(final Student student) method--------");
		StudentMongo studentMongo=convert(student);
		System.out.println("Converting (" + student + ") into (" + studentMongo + ")");
		return studentMongo;
	}
	
	public StudentMongo convert(Student student) {
		StudentMongo studentMongo = new StudentMongo();
		studentMongo.setId(student.getId());
		studentMongo.setStudentName(student.getStudentName());
		studentMongo.setStudentId(student.getStudentId());
		studentMongo.setPhoneNumber(student.getPhoneNumber());
		studentMongo.setAge(student.getAge());
		studentMongo.setLocation(student.getLocation());
		return studentMongo;
	}
}
