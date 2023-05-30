package com.example.EmployeeRegistration.service

import com.example.EmployeeRegistration.dto.EmployeeDTO
import com.example.EmployeeRegistration.entity.Employee
import com.example.EmployeeRegistration.exception.ApplicationException
import com.example.EmployeeRegistration.repository.EmployeeRepository
import spock.lang.Specification

import java.time.LocalDate
import java.time.format.DateTimeFormatter


class EmployeeServiceImplTest extends Specification {

    def "getAllEmployees should return list of employee DTOs"() {
        given:
        def employeeRepository = Mock(EmployeeRepository)
        def employeeService = new EmployeeServiceImpl(employeeRepository)
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        List<Employee> employees = [
                new Employee(id: 1, dob: LocalDate.parse("05/10/2000", dateFormatter), aadhar: "2234567890", name: "John Doe", age: 33, department: "IT", city: "New York"),
                new Employee(id: 2, dob: LocalDate.parse("05/10/2000", dateFormatter), aadhar: "9876543210", name: "Jane Smith", age: 28, department: "HR", city: "London")
        ]
        employeeRepository.findAll() >> employees

        when:
        List<EmployeeDTO> result = employeeService.getAllEmployees()

        then:
        result.size() == employees.size()
        result.collect { it.id } == employees.collect { it.id }
        result.collect { it.aadhar } == employees.collect { it.aadhar }
        result.collect { it.name } == employees.collect { it.name }
        result.collect { it.age } == employees.collect { it.age }
        result.collect { it.department } == employees.collect { it.department }
        result.collect { it.city } == employees.collect { it.city }
    }


    def "getAllEmployees should throw ApplicationException when employee repository is empty"() {
        given:
        def employeeRepository = Mock(EmployeeRepository)
        def employeeService = new EmployeeServiceImpl(employeeRepository)
        employeeRepository.findAll() >> []

        when:
        employeeService.getAllEmployees()

        then:
        def exception = thrown(ApplicationException)
        exception.message == "Employees_NOT_FOUND"
    }

    def "should update department of a employee when employee with given aadhar is present"() {
        given:
        def employeeRepository = Mock(EmployeeRepository)
        def employeeService = new EmployeeServiceImpl(employeeRepository)
        String aadhar = "2234567890"
        String updatedDepartment = "HR"
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        Employee existingEmployee = new Employee(id: 1, dob: LocalDate.parse("05/10/2000", dateFormatter), aadhar: aadhar, name: "John Doe", age: 33, department: "IT", city: "New York")

        when:
        employeeService.updateEmployee(aadhar,updatedDepartment)

        then:
        1 * employeeRepository.findByAadhar(aadhar) >> Optional.of(existingEmployee)
        existingEmployee.setDepartment(updatedDepartment)
        existingEmployee.department==updatedDepartment
    }
}

