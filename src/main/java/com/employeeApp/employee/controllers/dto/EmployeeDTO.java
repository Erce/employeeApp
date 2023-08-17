package com.employeeApp.employee.controllers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private UUID id;
    @NotEmpty
    @NotNull
    @Email
    private String email;
    @NotEmpty
    private String fullName;
    private String birthday;
    private List<String> hobbies;
}
