package com.employeeApp.employeeApi.services;

import com.employeeApp.employeeApi.entity.Employee;
import com.employeeApp.employeeApi.repositories.EmployeeRepository;
import com.employeeApp.employeeApi.kafka.EmployeeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private KafkaTemplate<String, EmployeeEvent> kafkaTemplate;

    @Override
    public Employee createEmployee(Employee employee) {
        employee.setId(UUID.randomUUID());
        Employee createdEmployee = employeeRepository.save(employee);

        // Create event and send
        EmployeeEvent employeeEvent = new EmployeeEvent();
        employeeEvent.setEmployeeId(employee.getId());
        employeeEvent.setEventType("EmployeeCreated");
        kafkaTemplate.send("employee-events", employeeEvent);

        return createdEmployee;
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
