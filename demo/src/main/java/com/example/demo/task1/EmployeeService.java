package com.example.demo.task1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	private final ExecutorService executor = Executors.newFixedThreadPool(10);

	public void processEmployees() {
		try (Stream<Employee> employeeStream = employeeRepository.findAllEmployees()) {
			employeeStream.forEach(employee -> executor.submit(() -> processEmployee(employee)));
		}
		executor.shutdown();
	}

	private void processEmployee(Employee employee) {
		System.out.println("Processing Employee ID: " + employee.getId() + ", Name: " + employee.getName());
	}

}