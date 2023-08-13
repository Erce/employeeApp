package com.employeeApp.employee.exception;

public class NoEmployeeExistsException extends RuntimeException{
    private String message;

    public NoEmployeeExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
