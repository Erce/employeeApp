package com.employeeApp.employeeApi.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEventListener {
    @KafkaListener(topics = "employee-events", groupId = "employee-group")
    public void listenEmployeeEvents(EmployeeEvent employeeEvent) {
        System.out.println("Received employee event: " + employeeEvent);
    }
}
