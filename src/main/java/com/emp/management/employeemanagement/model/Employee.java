package com.emp.management.employeemanagement.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;

    @Enumerated(EnumType.STRING)
    private EmployeeStates status;

}

