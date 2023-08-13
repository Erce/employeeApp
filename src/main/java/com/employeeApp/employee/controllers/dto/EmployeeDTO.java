package com.employeeApp.employee.controllers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class EmployeeDTO {
    private UUID id;
    @NotEmpty
    private String fullName;
    @NotEmpty
    @NotNull
    @Email
    private String email;
    private String birthday;
    private List<String> hobbies;
}
