package com.batch.MultiDB.model.mongo;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "StudentMongo")
public class StudentMongo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer id;

	private Integer StudentId;

	private String studentName;

	private Long phoneNumber;

	private Long age;

	private String location;

	public StudentMongo() {
	}

	public StudentMongo(Integer id, Integer studentId, String studentName, Long phoneNumber, Long age,
			String location) {
		super();
		this.id = id;
		StudentId = studentId;
		this.studentName = studentName;
		this.phoneNumber = phoneNumber;
		this.age = age;
		this.location = location;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStudentId() {
		return StudentId;
	}

	public void setStudentId(Integer studentId) {
		StudentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", StudentId=" + StudentId + ", studentName=" + studentName + ", phoneNumber="
				+ phoneNumber + ", age=" + age + ", location=" + location + "]";
	}

}
