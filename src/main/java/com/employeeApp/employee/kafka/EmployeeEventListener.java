package com.employeeApp.employee.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmployeeEventListener {
    @KafkaListener(topics = "employee-events", groupId = "employee-group")
    public void listenEmployeeEvents(EmployeeEvent employeeEvent) {
        log.info("Received employee event: " + employeeEvent);
    }
}
