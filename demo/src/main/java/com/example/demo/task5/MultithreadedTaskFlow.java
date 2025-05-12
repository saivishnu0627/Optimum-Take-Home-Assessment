package com.example.demo.task5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MultithreadedTaskFlow {

	private static final Logger logger = Logger.getLogger(MultithreadedTaskFlow.class.getName());

	// Task A
	private static String doTaskA() {
		try {
			logger.info("Executing Task A");
			TimeUnit.SECONDS.sleep(2); // Simulate processing time
			return "ResultA";
		} catch (InterruptedException e) {
			logger.severe("Task A interrupted");
			return "ErrorA";
		}
	}

	// Task B
	private static String doTaskB() {
		try {
			logger.info("Executing Task B");
			TimeUnit.SECONDS.sleep(2); // Simulate processing time
			return "ResultB";
		} catch (InterruptedException e) {
			logger.severe("Task B interrupted");
			return "ErrorB";
		}
	}

	// Processing output from A and B
	private static String processAB(String a, String b) {
		logger.info("Processing output from A & B");
		return a + "-" + b;
	}

	// Task C
	private static String doTaskC(String result) {
		if (result == null || result.contains("Error")) {
			throw new RuntimeException("Invalid input from A & B to Task C");
		}
		logger.info("Executing Task C with input: " + result);
		return "ProcessedC(" + result + ")";
	}

	// Task D
	private static String doTaskD(String result) {
		if (result == null || result.contains("Error")) {
			throw new RuntimeException("Invalid input from A & B to Task D");
		}
		logger.info("Executing Task D with input: " + result);
		return "ProcessedD(" + result + ")";
	}

	// Processing output from C and D
	private static String processCD(String c, String d) {
		logger.info("Combining results from C & D");
		return c + "+" + d;
	}

	// Final Task F
	private static void doTaskF(String result) {
		if (result == null || result.contains("Error")) {
			logger.severe("Final Task F received invalid input");
			return;
		}
		logger.info("Final processing in Task F with result: " + result);
	}

	public static void main(String[] args) {
		// Task A & B run in parallel
		CompletableFuture<String> taskA = CompletableFuture.supplyAsync(() -> doTaskA())
				.exceptionally(ex -> handleError("A", ex));

		CompletableFuture<String> taskB = CompletableFuture.supplyAsync(() -> doTaskB())
				.exceptionally(ex -> handleError("B", ex));

		// Combine A & B results and pass to C & D
		CompletableFuture<String> combinedAB = taskA.thenCombine(taskB, (a, b) -> processAB(a, b))
				.exceptionally(ex -> handleError("AB", ex));

		// Tasks C & D execute in parallel after A & B complete
		CompletableFuture<String> taskC = combinedAB.thenApplyAsync(result -> doTaskC(result))
				.exceptionally(ex -> handleError("C", ex));

		CompletableFuture<String> taskD = combinedAB.thenApplyAsync(result -> doTaskD(result))
				.exceptionally(ex -> handleError("D", ex));

		// Combine C & D results and pass to final task F
		CompletableFuture<String> combinedCD = taskC.thenCombine(taskD, (c, d) -> processCD(c, d))
				.exceptionally(ex -> handleError("CD", ex));

		// Final task F after C & D complete
		CompletableFuture<Void> finalTask = combinedCD.thenAccept(result -> doTaskF(result)).exceptionally(ex -> {
			logger.severe("Final Task F failed: " + ex.getMessage());
			return null;
		});

		// Timeout handling to avoid indefinite blocking
		finalTask.orTimeout(10, TimeUnit.SECONDS).handle((result, ex) -> {
			if (ex != null) {
				logger.severe("Operation timed out: " + ex.getMessage());
			} else {
				logger.info("Operation completed successfully within the timeout.");
			}
			return null;
		});

		// Wait for all tasks to complete
		try {
			finalTask.get();
			logger.info("All tasks completed successfully");
		} catch (InterruptedException | ExecutionException e) {
			logger.severe("Error in task execution: " + e.getMessage());
		}
	}

	// Fallback error handling
	private static String handleError(String taskName, Throwable ex) {
		logger.severe("Error in task " + taskName + ": " + ex.getMessage());
		return "FallbackResult-" + taskName;
	}

}
