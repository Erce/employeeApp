package com.employeeApp.employeeApi.repositories.dao;

import com.employeeApp.employeeApi.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeDAO {
    Employee saveEmployee(Employee employee);
    List<Employee> findAllEmployees();
    Employee findEmployeeById(UUID id);
    void deleteEmployee(UUID id);
}
