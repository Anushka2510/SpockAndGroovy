package com.example.EmployeeRegistration.service;

import com.example.EmployeeRegistration.dto.EmployeeDTO;
import com.example.EmployeeRegistration.entity.Employee;
import com.example.EmployeeRegistration.exception.AgeNotMatchingBasedOnDOBException;
import com.example.EmployeeRegistration.exception.ApplicationException;
import com.example.EmployeeRegistration.exception.DuplicateEmployeeException;
import com.example.EmployeeRegistration.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service(value = "employeeService")
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() throws ApplicationException {
        Iterable<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        employees.forEach(employee -> {
            EmployeeDTO EmployeeDTO = new EmployeeDTO();
            EmployeeDTO.setId(employee.getId());
            EmployeeDTO.setDob(employee.getDob());
            EmployeeDTO.setAadhar(employee.getAadhar());
            EmployeeDTO.setName(employee.getName());
            EmployeeDTO.setAge(employee.getAge());
            EmployeeDTO.setDepartment(employee.getDepartment());
            EmployeeDTO.setCity(employee.getCity());
            employeeDTOS.add(EmployeeDTO);
        });
        if (employeeDTOS.isEmpty())
            throw new ApplicationException("Employees_NOT_FOUND");
        return employeeDTOS;
    }
    @Override
    public EmployeeDTO getEmployee(Integer employeeId) throws ApplicationException {
        Optional<Employee> optional = employeeRepository.findById(employeeId);
        Employee Employee = optional.orElseThrow(() -> new ApplicationException("Employee does not exist"));
        EmployeeDTO EmployeeDTO = new EmployeeDTO();
        EmployeeDTO.setId(Employee.getId());
        EmployeeDTO.setDob(Employee.getDob());
        EmployeeDTO.setName(Employee.getName());
        EmployeeDTO.setCity(Employee.getCity());
        EmployeeDTO.setAge(Employee.getAge());
        EmployeeDTO.setAadhar(Employee.getAadhar());
        EmployeeDTO.setDepartment(EmployeeDTO.getDepartment());
        return EmployeeDTO;
    }
    @Override
    public EmployeeDTO getEmployee(String aadhar) throws ApplicationException {
        Optional<Employee> optional = employeeRepository.findByAadhar(aadhar);
        Employee Employee = optional.orElseThrow(() -> new ApplicationException("Employee does not exist"));
        EmployeeDTO EmployeeDTO = new EmployeeDTO();
        EmployeeDTO.setId(Employee.getId());
        EmployeeDTO.setDob(Employee.getDob());
        EmployeeDTO.setName(Employee.getName());
        EmployeeDTO.setCity(Employee.getCity());
        EmployeeDTO.setAge(Employee.getAge());
        EmployeeDTO.setAadhar(Employee.getAadhar());
        EmployeeDTO.setDepartment(Employee.getDepartment());
        return EmployeeDTO;
    }

   @Override
    public void updateEmployee(String aadhar, String department) throws ApplicationException{
        Optional<Employee> employee = employeeRepository.findByAadhar(aadhar);
        Employee e = employee.orElseThrow(() -> new ApplicationException("Employee does not exist for this Aadhar"));
        e.setDepartment(department);

    }

    @Override
    public Integer addEmployee(EmployeeDTO employeeDTO) throws ApplicationException {
        Employee employee = new Employee();
        employee.setDob(employeeDTO.getDob());
        employee.setName(employeeDTO.getName());
        employee.setId(employeeDTO.getId());
        if (employeeRepository.existsByAadhar(employeeDTO.getAadhar())) {
            throw new DuplicateEmployeeException("Employee with Aadhar number " + employee.getAadhar() + " already exists");
        }
        else {
            employee.setAadhar(employeeDTO.getAadhar());
        }

        String dateString = employeeDTO.getDob().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[] dobParts = dateString.split("/");
        int day = Integer.parseInt(dobParts[0]);
        int month = Integer.parseInt(dobParts[1]);
        int year = Integer.parseInt(dobParts[2]);
        LocalDate dob = LocalDate.of(year, month, day);
        int age = calculateAge(dob);
        if(employeeDTO.getAge()==age) {
            employee.setAge(employeeDTO.getAge());
        }
        else{
            throw new AgeNotMatchingBasedOnDOBException("Age of employee is wrong based on dob");
        }

        employee.setCity(employeeDTO.getCity());
        employee.setDepartment(employeeDTO.getDepartment());
        employeeRepository.save(employee);
        return employee.getId();
    }
    public static int calculateAge(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dob, currentDate).getYears();
    }

}
