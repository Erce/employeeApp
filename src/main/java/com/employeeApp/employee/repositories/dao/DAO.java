package com.employeeApp.employee.repositories.dao;

import java.util.List;
import java.util.UUID;

public interface DAO<T> {
    T saveEmployee(T t);
    List<T> findAllEmployees();
    T findEmployeeById(UUID id);
    void deleteEmployee(UUID id);
}
