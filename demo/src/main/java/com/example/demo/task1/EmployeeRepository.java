package com.example.demo.task1;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Transactional(readOnly = true)
	@Query("SELECT e FROM Employee e")
	Stream<Employee> findAllEmployees();

}