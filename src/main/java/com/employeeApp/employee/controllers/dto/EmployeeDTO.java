package com.employeeApp.employee.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private List<String> hobbies;
}
