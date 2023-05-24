package com.example.EmployeeRegistration.repository;

import com.example.EmployeeRegistration.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    boolean existsByAadhar(String aadhar);

    Optional<Employee> findByAadhar(String aadhar);
}
