package com.employeeApp.employeeApi.kafka;

import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeEvent {
    private UUID employeeId;
    private String eventType;
}
