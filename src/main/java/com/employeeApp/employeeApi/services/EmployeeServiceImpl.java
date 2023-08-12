package com.employeeApp.employeeApi.services;

import com.employeeApp.employeeApi.domain.Employee;
import com.employeeApp.employeeApi.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        employee.setId(UUID.randomUUID());
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployee(UUID id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
