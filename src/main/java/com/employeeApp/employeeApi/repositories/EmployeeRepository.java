package com.employeeApp.employeeApi.repositories;

import com.employeeApp.employeeApi.domain.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface EmployeeRepository extends MongoRepository<Employee, UUID> {
    Employee findByFullName(String fullName);
}
