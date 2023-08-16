package com.employeeApp.employee.unit.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.employeeApp.employee.controllers.dto.EmployeeDTO;
import com.employeeApp.employee.entity.Employee;
import com.employeeApp.employee.kafka.EmployeeEvent;
import com.employeeApp.employee.repositories.dao.employee.EmployeeDAOImpl;
import com.employeeApp.employee.services.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private KafkaTemplate<String, EmployeeEvent> kafkaTemplate;

    @Mock
    private EmployeeDAOImpl employeeDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEmployee() {
        UUID employeeId = UUID.randomUUID();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employeeId);
        employeeDTO.setFullName("Test employee");
        employeeDTO.setEmail("testemployee@test.com");
        Employee createdEmployee = new Employee();
        createdEmployee.setId(employeeDTO.getId());
        createdEmployee.setFullName(employeeDTO.getFullName());
        createdEmployee.setEmail(employeeDTO.getEmail());

        when(employeeDAO.saveEmployee(any(Employee.class))).thenReturn(createdEmployee);

        EmployeeDTO result = employeeService.createEmployee(employeeDTO);

        assertNotNull(result);
        assertEquals(employeeDTO.getEmail(), result.getEmail());
        assertEquals(employeeDTO.getFullName(), result.getFullName());
    }

    @Test
    public void testGetEmployee() {
        UUID employeeId = UUID.randomUUID();
        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setFullName("Test Employee 1");
        existingEmployee.setEmail("testemployee1@test.com");

        when(employeeDAO.findEmployeeById(employeeId)).thenReturn(existingEmployee);

        EmployeeDTO retrievedEmployee = employeeService.getEmployee(employeeId);

        assertNotNull(retrievedEmployee);
        assertEquals(existingEmployee.getId(), retrievedEmployee.getId());
        assertEquals(existingEmployee.getEmail(), retrievedEmployee.getEmail());
        assertEquals(existingEmployee.getEmail(), retrievedEmployee.getEmail());
    }

    @Test
    public void testUpdateEmployee() {
        UUID employeeId = UUID.randomUUID();
        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        existingEmployee.setFullName("Test Employee 1");
        existingEmployee.setEmail("testemployee1@test.com");
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setId(employeeId);
        updatedEmployeeDTO.setFullName("Test Employee 1");
        updatedEmployeeDTO.setEmail("testemployeeNewEmail@test.com");

        when(employeeDAO.findEmployeeById(employeeId)).thenReturn(existingEmployee);
        when(employeeDAO.saveEmployee(any(Employee.class))).thenReturn(existingEmployee);

        EmployeeDTO updatedEmployee = employeeService.updateEmployee(employeeId, updatedEmployeeDTO);

        assertNotNull(updatedEmployee);
        assertEquals(existingEmployee.getId(), updatedEmployee.getId());
        assertEquals(existingEmployee.getEmail(), updatedEmployee.getEmail());
        assertEquals(updatedEmployeeDTO.getEmail(), updatedEmployee.getEmail());
    }

    @Test
    public void testDeleteEmployee() {
        UUID employeeId = UUID.randomUUID();

        employeeService.deleteEmployee(employeeId);

        verify(employeeDAO, times(1)).deleteEmployee(employeeId);
    }

    @Test
    public void testGetAllEmployees() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        employee1.setFullName("Test Employee 1");
        employee1.setEmail("testemployee1@test.com");
        employee2.setFullName("Test Employee 2");
        employee2.setEmail("testemployee2@test.com");

        List<Employee> employeeList = Arrays.asList(employee1, employee2);

        when(employeeDAO.findAllEmployees()).thenReturn(employeeList);

        List<EmployeeDTO> retrievedEmployees = employeeService.getAllEmployees();

        assertNotNull(retrievedEmployees);
        assertEquals(employeeList.size(), retrievedEmployees.size());

        assertEquals(employeeList.get(0).getId(), retrievedEmployees.get(0).getId());
        assertEquals(employeeList.get(1).getEmail(), retrievedEmployees.get(1).getEmail());
    }
}
