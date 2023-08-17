package com.employeeApp.employee.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.employeeApp.employee.controllers.dto.EmployeeDTO;
import com.employeeApp.employee.entity.Employee;
import com.employeeApp.employee.init.MongoContainerBase;
import com.employeeApp.employee.kafka.EmployeeEvent;
import com.employeeApp.employee.services.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntTest extends MongoContainerBase {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private KafkaTemplate<String, EmployeeEvent> kafkaTemplate;
    private EmployeeDTO employee1, employee2;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        employee1 = new EmployeeDTO(
                UUID.randomUUID(),
                "ercecanbalcioglu@gmail.com",
                "Erce Can Balcioglu",
                LocalDate.parse("1992-03-30", DateTimeFormatter.ISO_DATE),
                Arrays.asList("gaming")
        );

        employee2 = new EmployeeDTO(
                UUID.randomUUID(),
                "test@test.com",
                "Test TEST",
                LocalDate.parse("1992-03-30", DateTimeFormatter.ISO_DATE),
                Arrays.asList("reading", "cycling")
        );
    }

    @AfterEach
    public void tearDown() {
        // Clean up the database after each test
        mongoTemplate.dropCollection("employee");
        mongoTemplate.createCollection("employee");
    }

    @Test
    public void shouldCreateEmployee() throws Exception {
        // Define the behavior of the mocked EmployeeService
        String jsonBody = objectMapper.writeValueAsString(employee1);
        // Perform the HTTP request and assertions
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andReturn();


        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        // Setting the actual id to the employee
        Employee createdEmployee = objectMapper.readValue(actualResponseBody, Employee.class);
        employee1.setId(createdEmployee.getId());
        jsonBody = objectMapper.writeValueAsString(employee1);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(jsonBody);
    }

    @Test
    public void shouldUpdateEmployeeWithGivenId() throws Exception {
        // Create employee in the test database
        EmployeeDTO createdEmployee = employeeService.createEmployee(employee1);
        employee2.setId(createdEmployee.getId());
        employee2.setEmail("test2@test.com");
        String updatedJsonBody = objectMapper.writeValueAsString(employee2);

        // Perform the HTTP request and assertions
        MvcResult mvcUpdatedResult = mockMvc.perform(put("/employees/{id}", employee2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJsonBody))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcUpdatedResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(updatedJsonBody);
    }

    @Test
    public void shouldGetAllEmployees() throws Exception {
        // Create employee1 in the test database
        EmployeeDTO createdEmployee1 = employeeService.createEmployee(employee1);
        EmployeeDTO createdEmployee2 = employeeService.createEmployee(employee2);
        String jsonBodyEmployeeString = objectMapper.writeValueAsString(Arrays.asList(createdEmployee1, createdEmployee2));

        // Get all employees
        MvcResult mvcResultAllEmployees = mockMvc.perform(MockMvcRequestBuilders
                        .get("/employees"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResultAllEmployees.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(jsonBodyEmployeeString);
    }

    @Test
    public void shouldGetEmployeeWithGivenId() throws Exception {
        // Create employee1 in the test database
        EmployeeDTO createdEmployee = employeeService.createEmployee(employee1);
        employee1.setId(createdEmployee.getId());

        // Perform the HTTP request and assertions
        MvcResult mvcResult = mockMvc.perform(get("/employees/{id}", employee1.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(employee1));
    }

    @Test
    public void shouldDeleteEmployee() throws Exception {
        EmployeeDTO employeeDTO = employeeService.createEmployee(employee1);

        // Perform the HTTP request and assertions
        mockMvc.perform(delete("/employees/{id}", employeeDTO.getId()))
                .andExpect(status().isNoContent());
    }
}
