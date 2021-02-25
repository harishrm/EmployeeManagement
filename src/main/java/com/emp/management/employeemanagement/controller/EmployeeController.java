package com.emp.management.employeemanagement.controller;

import com.emp.management.employeemanagement.model.Employee;
import com.emp.management.employeemanagement.model.EmployeeEvents;
import com.emp.management.employeemanagement.model.EmployeeStates;
import com.emp.management.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/createemployee")
    public ResponseEntity<?>createEmployee(@RequestBody Employee emp){
        System.out.println(emp.toString());
        if(!employeeService.isEmployeeExistsByEmail(emp.getEmployeeEmail())) {
            System.out.println(emp.toString());
            Employee employe=new Employee();
            employe.setEmployeeName(emp.getEmployeeName());
            employe.setStatus(EmployeeStates.ADDED);
            employe.setEmployeeEmail(emp.getEmployeeEmail());

            employeeService.savOrUpdateEmployee(employe);
            return new ResponseEntity<>(employe,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Employee with email already exists",HttpStatus.CONFLICT);
        }
    }
    @PostMapping("/updateemployeeestatus/{status}")
    public ResponseEntity<?>updateEmployee(@RequestBody Employee emp, @PathVariable String status){
        if(employeeService.isEmployeeExistsByEmail(emp.getEmployeeEmail())) {
            Optional<Employee> optEmp=employeeService.findByEmailId(emp.getEmployeeEmail());

            employeeService.updateStatus(optEmp.get().getEmployeeId(),status);
            return new ResponseEntity<>("Status updated successfully",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Employee with email not found",HttpStatus.CONFLICT);
        }
        }
    }

