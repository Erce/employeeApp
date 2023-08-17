package com.employeeApp.employee.integration.dao;

import com.employeeApp.employee.entity.Employee;
import com.employeeApp.employee.exception.EmployeeAlreadyExistsException;
import com.employeeApp.employee.exception.NoEmployeeExistsException;
import com.employeeApp.employee.init.MongoContainerBase;
import com.employeeApp.employee.repositories.dao.employee.EmployeeDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Testcontainers
public class EmployeeDAOTest extends MongoContainerBase {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    EmployeeDAO employeeDAO;
    private Employee createdEmployee;

    @BeforeEach
    public void setUp() {
        // Create an employee for testing
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee(
                employeeId,
                "ercecanbalcioglu@gmail.com",
                "Erce Can Balcioglu",
                LocalDate.parse("1992-03-30", DateTimeFormatter.ISO_DATE),
                Arrays.asList("gym", "gaming")
        );
        createdEmployee = employeeDAO.saveEmployee(employee);
    }

    @AfterEach
    public void tearDown() {
        // Clean up the database after each test
        employeeDAO.deleteEmployee(createdEmployee.getId());
        mongoTemplate.dropCollection("employee");
        mongoTemplate.createCollection("employee");
    }

    @Test
    void shouldSaveEmployeeWhenProvidedWithDetails(){
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee(
                employeeId,
                "test@test.com",
                "Test TEST",
                LocalDate.parse("1992-03-30", DateTimeFormatter.ISO_DATE),
                Arrays.asList("tennis", "basketball", "swimming")
        );
        Employee createdEmployee = employeeDAO.saveEmployee(employee);
        assert(createdEmployee.getId().equals(employee.getId()));
    }

    @Test
    void shouldThrowExceptionWhenEmployeeSavedWithAnExistingEmail(){
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee(
                employeeId,
                "ercecanbalcioglu@gmail.com",
                "Erce Can Balcioglu",
                LocalDate.parse("1992-03-30", DateTimeFormatter.ISO_DATE),
                Arrays.asList("gym", "gaming")
        );
        assertThrows(EmployeeAlreadyExistsException.class, () -> employeeDAO.saveEmployee(employee));
    }

    @Test
    void shouldThrowExceptionWhenQueriedWithIdGivenEmployeeIsNotPresent(){
        assertThrows(NoEmployeeExistsException.class, () -> employeeDAO.findEmployeeById(UUID.randomUUID()));
    }

    @Test
    void shouldGetEmployeeWhenQueriedWithIdGivenEmployeeIsPresent(){
        Employee queriedEmployee = employeeDAO.findEmployeeById(createdEmployee.getId());
        assert(queriedEmployee.getFullName().equals(createdEmployee.getFullName()));
        assert(queriedEmployee.getEmail().equals(createdEmployee.getEmail()));
    }

    @Test
    void shouldGetAllEmployeesWhenQueriedAllEmployees(){
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee(
                employeeId,
                "test2@test.com",
                "Test2 TEST",
                LocalDate.parse("1992-03-30", DateTimeFormatter.ISO_DATE),
                Arrays.asList("tennis", "basketball", "swimming")
        );
        Employee createdEmployee2 = employeeDAO.saveEmployee(employee);
        List<Employee> queriedAllEmployees = employeeDAO.findAllEmployees();

        assertEquals(createdEmployee.getId(), queriedAllEmployees.get(0).getId());
        assertEquals(createdEmployee2.getId(), queriedAllEmployees.get(1).getId());
    }

    @Test
    void shouldUpdateEmployeeWhenEmailIsChangedGivenEmployeeIsPresent(){
        String newEmail = "test@test.com";
        createdEmployee.setEmail(newEmail);
        Employee updatedEmployee = employeeDAO.saveEmployee(createdEmployee);
        assertEquals(updatedEmployee.getEmail(), newEmail);
    }

    @Test
    void shouldDeleteEmployeeWhenEmployeeIdIsProvidedGivenEmployeeIsPresent(){
        Employee queriedEmployee = employeeDAO.findEmployeeById(createdEmployee.getId());
        assertEquals(queriedEmployee.getId(), createdEmployee.getId());
        employeeDAO.deleteEmployee(createdEmployee.getId());
        assertThrows(NoEmployeeExistsException.class, () -> employeeDAO.findEmployeeById(createdEmployee.getId()));
    }

    @Test
    void shouldThrowExceptionWhenEmployeeIdIsProvidedGivenEmployeeIsNotPresent(){
        UUID randomId = UUID.randomUUID();
        assertThrows(NoEmployeeExistsException.class, () -> employeeDAO.findEmployeeById(randomId));
    }
}
