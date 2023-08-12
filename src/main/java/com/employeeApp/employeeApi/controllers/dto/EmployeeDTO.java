package com.employeeApp.employeeApi.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class EmployeeDTO {
    private UUID id;
    @NotBlank(message = "Name cannot be blank.")
    private String fullName;
    @NotBlank(message = "Email cannot be blank.")
    private String email;
    private String birthday;
    private List<String> hobbies;
}
