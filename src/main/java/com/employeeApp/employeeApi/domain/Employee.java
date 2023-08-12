package com.employeeApp.employeeApi.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Indexed(unique = true, sparse=true)
    private String email;
    private String fullName;
    private String birthday;
    private List<String> hobbies;

    public Employee(
            String fullName,
            String email
    ) {
        this.fullName = fullName;
        this.email = email;
    }

    public Employee(
        String fullName,
        String email,
        String birthday,
        List<String> hobbies
    ) {
        this.fullName = fullName;
        this.email = email;
        this.birthday = birthday;
        this.hobbies = hobbies;
    }
}
