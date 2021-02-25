package com.emp.management.employeemanagement.service;

import com.emp.management.employeemanagement.model.Employee;
import com.emp.management.employeemanagement.model.EmployeeEvents;
import com.emp.management.employeemanagement.model.EmployeeStates;
import org.springframework.statemachine.StateMachine;

import java.util.Optional;

public interface EmployeeService {
    public Employee savOrUpdateEmployee(Employee emp);
    public Boolean isEmployeeExistsByEmail(String emailId);
    public Optional<Employee> findByEmailId(String emailId);
    public void updateStatus(Long empId,String event);
}
