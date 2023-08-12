package com.employeeApp.employeeApi.services;

import com.employeeApp.employeeApi.controllers.dto.EmployeeDTO;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO getEmployee(UUID id);
    List<EmployeeDTO> getAllEmployees();
    void deleteEmployee(UUID id);
    EmployeeDTO updateEmployee(UUID id, EmployeeDTO employeeDTO);
}
