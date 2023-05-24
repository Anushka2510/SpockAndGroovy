package com.example.EmployeeRegistration.service;

import com.example.EmployeeRegistration.dto.EmployeeDTO;
import com.example.EmployeeRegistration.exception.ApplicationException;

import java.util.List;

public interface EmployeeService {
    Integer addEmployee(EmployeeDTO EmployeeDTO) throws ApplicationException;

    EmployeeDTO getEmployee(Integer EmployeeId) throws ApplicationException;
    EmployeeDTO getEmployee(String Aadhar) throws ApplicationException;

    void updateEmployee(String Aadhar, String department) throws ApplicationException;
    

    List<EmployeeDTO> getAllEmployees() throws ApplicationException;
    

}
