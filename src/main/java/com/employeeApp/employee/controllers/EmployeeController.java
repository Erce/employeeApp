package com.employeeApp.employee.controllers;

import com.employeeApp.employee.controllers.dto.EmployeeDTO;
import com.employeeApp.employee.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class EmployeeController {
    private final EmployeeService employeeService;
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            employeeService.createEmployee(employeeDTO)
        );
    }

    @RequestMapping(value = "/employees/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            employeeService.getEmployee(id)
        );
    }

    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.status(HttpStatus.OK).body(
                employeeService.getAllEmployees()
        );
    }

    @RequestMapping(value = "/employees/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
    }

    @RequestMapping(value = "/employees/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable UUID id,@Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                employeeService.updateEmployee(id, employeeDTO)
        );
    }
}
