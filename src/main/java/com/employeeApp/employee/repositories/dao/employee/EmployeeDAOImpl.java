package com.employeeApp.employee.repositories.dao.employee;

import com.employeeApp.employee.entity.Employee;
import com.employeeApp.employee.exception.EmployeeAlreadyExistsException;
import com.employeeApp.employee.exception.NoEmployeeExistsException;
import com.employeeApp.employee.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        Employee existingEmployee = employeeRepository.findByEmail(employee.getEmail());

        if (existingEmployee == null) {
            return employeeRepository.save(employee);
        }
        throw new EmployeeAlreadyExistsException("Employee with the given email already exists!");
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findEmployeeById(UUID id) {
        return employeeRepository.findById(id).orElseThrow(()
                -> new NoEmployeeExistsException("No employee with the given id exists!"));
    }

    @Override
    public void deleteEmployee(UUID id) {
        employeeRepository.deleteById(id);
    }
}
