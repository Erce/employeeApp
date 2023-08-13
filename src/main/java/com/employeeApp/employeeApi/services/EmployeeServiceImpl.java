package com.employeeApp.employeeApi.services;

import com.employeeApp.employeeApi.controllers.dto.EmployeeDTO;
import com.employeeApp.employeeApi.entity.Employee;
import com.employeeApp.employeeApi.kafka.EmployeeEvent;
import com.employeeApp.employeeApi.repositories.dao.EmployeeDAO;
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
    private EmployeeDAO employeeDAO;
    @Autowired
    private KafkaTemplate<String, EmployeeEvent> kafkaTemplate;

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setId(UUID.randomUUID());
        Employee createdEmployee = employeeDAO.saveEmployee(employee);
        employeeDTO.setId(createdEmployee.getId());

        // Create event and send
        EmployeeEvent employeeEvent = new EmployeeEvent();
        employeeEvent.setEmployeeId(employee.getId());
        employeeEvent.setEventType("EmployeeCreated");

        kafkaTemplate.send("employee-events", employeeEvent);

        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeDAO.findAllEmployees();
        return employees.stream()
                .map(employee -> {
                    EmployeeDTO employeeDTO = new EmployeeDTO();
                    BeanUtils.copyProperties(employee, employeeDTO);
                    return employeeDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployee(UUID id) {
        Employee employee = employeeDAO.findEmployeeById(id);
        if (employee != null) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            BeanUtils.copyProperties(employee, employeeDTO);
            return employeeDTO;
        }
        return null;
    }

    @Override
    public void deleteEmployee(UUID id) {
        employeeDAO.deleteEmployee(id);

        // Create event and send
        EmployeeEvent employeeEvent = new EmployeeEvent();
        employeeEvent.setEmployeeId(id);
        employeeEvent.setEventType("EmployeeDeleted");

        kafkaTemplate.send("employee-events", employeeEvent);
    }

    @Override
    public EmployeeDTO updateEmployee(UUID id, EmployeeDTO employeeDTO) {
        Employee employee = employeeDAO.findEmployeeById(id);

        if(employee != null) {
            BeanUtils.copyProperties(employeeDTO, employee);
            employee.setId(id);
            employeeDAO.saveEmployee(employee);
            employeeDTO.setId(id);

            // Create event and send
            EmployeeEvent employeeEvent = new EmployeeEvent();
            employeeEvent.setEmployeeId(id);
            employeeEvent.setEventType("EmployeeUpdated");

            kafkaTemplate.send("employee-events", employeeEvent);

            return employeeDTO;
        }

        return null;
    }
}
