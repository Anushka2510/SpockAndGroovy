package com.example.EmployeeRegistration.exception;

public class AgeNotMatchingBasedOnDOBException extends RuntimeException {
    public AgeNotMatchingBasedOnDOBException(String message) {
        super(message);
    }
}
