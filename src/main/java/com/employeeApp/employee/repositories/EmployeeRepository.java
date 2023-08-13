package com.employeeApp.employee.repositories;

import com.employeeApp.employee.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, UUID> {
    Employee findByFullName(String fullName);
    Employee findByEmail(String email);
    void deleteById(@Nullable UUID id);
}
