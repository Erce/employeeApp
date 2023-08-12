package com.employeeApp.employeeApi.services;

import com.employeeApp.employeeApi.controllers.dto.EmployeeDTO;
import com.employeeApp.employeeApi.entity.Employee;
import com.employeeApp.employeeApi.repositories.EmployeeRepository;
import com.employeeApp.employeeApi.kafka.EmployeeEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private KafkaTemplate<String, EmployeeEvent> kafkaTemplate;

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setId(UUID.randomUUID());
        employeeRepository.save(employee);

        // Create event and send
        EmployeeEvent employeeEvent = new EmployeeEvent();
        employeeEvent.setEmployeeId(employee.getId());
        employeeEvent.setEventType("EmployeeCreated");

        kafkaTemplate.send("employee-events", employeeEvent);

        return employeeDTO;
    }

    @Override
    public EmployeeDTO getEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee != null) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            BeanUtils.copyProperties(employee, employeeDTO);
            return employeeDTO;
        }
        return null;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> {
                    EmployeeDTO employeeDTO = new EmployeeDTO();
                    BeanUtils.copyProperties(employee, employeeDTO);
                    return employeeDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEmployee(UUID id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeDTO updateEmployee(UUID id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id).orElse(null);

        if(employee != null) {
            BeanUtils.copyProperties(employeeDTO, employee);
            employee.setId(id);
            employeeRepository.save(employee);

            return employeeDTO;
        }

        return null;
    }
}
