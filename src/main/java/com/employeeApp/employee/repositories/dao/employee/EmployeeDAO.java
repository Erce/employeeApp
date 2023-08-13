package com.employeeApp.employee.repositories.dao.employee;

import com.employeeApp.employee.entity.Employee;
import com.employeeApp.employee.repositories.dao.DAO;

import java.util.List;
import java.util.UUID;

public interface EmployeeDAO extends DAO<Employee> {
    Employee saveEmployee(Employee employee);
    List<Employee> findAllEmployees();
    Employee findEmployeeById(UUID id);
    void deleteEmployee(UUID id);
}
