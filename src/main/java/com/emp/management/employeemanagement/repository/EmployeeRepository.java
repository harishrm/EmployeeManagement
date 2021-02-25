package com.emp.management.employeemanagement.repository;

import com.emp.management.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Boolean existsByEmployeeEmail(String email);
    Optional<Employee> findByEmployeeEmail(String email);
}
