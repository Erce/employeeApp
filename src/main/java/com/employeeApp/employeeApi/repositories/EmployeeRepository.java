package com.employeeApp.employeeApi.repositories;

import com.employeeApp.employeeApi.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, UUID> {
    Employee findByFullName(String fullName);
    void deleteById(@Nullable UUID id);
}
