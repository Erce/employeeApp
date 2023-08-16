package com.employeeApp.employee.unit.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.employeeApp.employee.controllers.EmployeeController;
import com.employeeApp.employee.controllers.dto.EmployeeDTO;
import com.employeeApp.employee.services.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;
    private EmployeeDTO mockedEmployee1, mockedEmployee2;

    private EmployeeController employeeController;

    @BeforeEach
    public void setUp() {
        employeeController = new EmployeeController(employeeService);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();

        // Mock API_KEY authentication
        Authentication auth = new TestingAuthenticationToken(null, null, "API_KEY");
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockedEmployee1 = new EmployeeDTO(
                UUID.randomUUID(),
                "Erce Can Balcioglu",
                "ercecanbalcioglu@gmail.com",
                "1992-03-30",
                Arrays.asList("gaming")
        );

        mockedEmployee2 = new EmployeeDTO(
                UUID.randomUUID(),
                "Test TEST",
                "test@test.com",
                "1949-01-14",
                Arrays.asList("reading", "cycling")
        );
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void shouldCreateEmployee() throws Exception {
        // Define the behavior of the mocked EmployeeService
        String jsonBody = objectMapper.writeValueAsString(mockedEmployee1);
        when(employeeService.createEmployee(any(EmployeeDTO.class))).thenReturn(mockedEmployee1);

        // Perform the HTTP request and assertions
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockedEmployee1.getId().toString()))
                .andExpect(jsonPath("$.fullName").value(mockedEmployee1.getFullName()))
                .andExpect(jsonPath("$.email").value(mockedEmployee1.getEmail()))
                .andExpect(jsonPath("$.birthday").value(mockedEmployee1.getBirthday()))
                .andExpect(jsonPath("$.hobbies[0]").value(mockedEmployee1.getHobbies().get(0)));
    }

    @Test
    public void shouldUpdateEmployeeWithGivenId() throws Exception {
        // Define the behavior of the mocked EmployeeService
        mockedEmployee1.setEmail("test@test.com");
        when(employeeService.updateEmployee(any(UUID.class), any(EmployeeDTO.class))).thenReturn(mockedEmployee1);

        String jsonBody = objectMapper.writeValueAsString(mockedEmployee1);

        // Perform the HTTP request and assertions
        mockMvc.perform(put("/employees/{id}", mockedEmployee1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockedEmployee1.getId().toString()))
                .andExpect(jsonPath("$.fullName").value(mockedEmployee1.getFullName()))
                .andExpect(jsonPath("$.email").value(mockedEmployee1.getEmail()))
                .andExpect(jsonPath("$.birthday").value(mockedEmployee1.getBirthday()))
                .andExpect(jsonPath("$.hobbies[0]").value(mockedEmployee1.getHobbies().get(0)));
    }

    @Test
    public void shouldGetAllEmployees() throws Exception {
        // Define the behavior of the mocked EmployeeService
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(mockedEmployee1, mockedEmployee2));

        // Create JSON objects and convert them to strings
        String jsonEmployee1 = objectMapper.writeValueAsString(mockedEmployee1);
        String jsonEmployee2 = objectMapper.writeValueAsString(mockedEmployee2);

        // Perform the HTTP request and assertions
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" + jsonEmployee1 + "," + jsonEmployee2 + "]"));
    }

    @Test
    public void shouldGetEmployeeWithGivenId() throws Exception {
        // Define the behavior of the mocked EmployeeService
        when(employeeService.getEmployee(mockedEmployee1.getId())).thenReturn(mockedEmployee1);

        // Perform the HTTP request and assertions
        mockMvc.perform(get("/employees/{id}", mockedEmployee1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockedEmployee1.getId().toString()))
                .andExpect(jsonPath("$.fullName").value(mockedEmployee1.getFullName()))
                .andExpect(jsonPath("$.email").value(mockedEmployee1.getEmail()))
                .andExpect(jsonPath("$.birthday").value(mockedEmployee1.getBirthday()))
                .andExpect(jsonPath("$.hobbies[0]").value(mockedEmployee1.getHobbies().get(0)));
    }

    @Test
    public void shouldDeleteEmployee() throws Exception {
        UUID employeeIdToDelete = UUID.randomUUID();

        // Perform the HTTP request and assertions
        mockMvc.perform(delete("/employees/{id}", employeeIdToDelete))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(eq(employeeIdToDelete));
    }
}
