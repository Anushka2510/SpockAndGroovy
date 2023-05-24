package com.example.EmployeeRegistration.controller;

import com.example.EmployeeRegistration.dto.EmployeeDTO;
import com.example.EmployeeRegistration.dto.EmployeeUpdateDTO;
import com.example.EmployeeRegistration.exception.ApplicationException;
import com.example.EmployeeRegistration.service.EmployeeService;
import com.example.EmployeeRegistration.utility.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/employee")
    public ResponseEntity<String> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) throws ApplicationException {
        Integer Id = employeeService.addEmployee(employeeDTO);
        String successMessage = "INSERT_SUCCESS " + Id;
        return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
    }

    @GetMapping(value = "/employee")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() throws ApplicationException {
        List<EmployeeDTO> customerList = employeeService.getAllEmployees();
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }

    @GetMapping(value = "/employees/id/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeDetails(@PathVariable @Min(value = 1, message = "{Employee EmployeeId invalid}") @Max(value = 100, message = "{Employee EmployeeId invalid}") Integer EmployeeId) throws ApplicationException {
        EmployeeDTO customer = employeeService.getEmployee(EmployeeId);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping(value = "/employees/{employeeAadhar}")
    public ResponseEntity<EmployeeDTO> getEmployeeDetailsByAadhar(@PathVariable String employeeAadhar) throws ApplicationException {
        EmployeeDTO customer = employeeService.getEmployee(employeeAadhar);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }


    @PutMapping(value = "/employees/{employeeAadhar}")
    public ResponseEntity<Object> updateEmployee(@PathVariable String employeeAadhar, @Valid @RequestBody EmployeeUpdateDTO employee)
            throws ApplicationException {
        employeeService.updateEmployee(employeeAadhar, employee.getDepartment());
        String successMessage = "UPDATE_SUCCESS";
        return ResponseHandler.generateResponse(successMessage, HttpStatus.OK);
    }

}
