package com.example.EmployeeRegistration.entity;



import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "employeetable")
public class Employee {
 @Id
 @Column(name = "id", nullable = false)
 private Integer id;

 private String name;

 private String aadhar;

 private Integer age;

 private String department;
 private LocalDate dob;

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getAadhar() {
  return aadhar;
 }

 public void setAadhar(String aadhar) {
  this.aadhar = aadhar;
 }

 public int getAge() {
  return age;
 }

 public void setAge(int age) {
  this.age = age;
 }

 public String getDepartment() {
  return department;
 }

 public void setDepartment(String department) {
  this.department = department;
 }

 public String getCity() {
  return city;
 }

 public void setCity(String city) {
  this.city = city;
 }

 public LocalDate getDob() {
  return dob;
 }

 public void setDob(LocalDate dob) {
  this.dob = dob;
 }

 private String city;



 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
 }

}
