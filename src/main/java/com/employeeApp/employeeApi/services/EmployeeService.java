package com.employeeApp.employeeApi.services;

import com.employeeApp.employeeApi.domain.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    Employee getEmployee(UUID id);
    List<Employee> getAllEmployees();
}
