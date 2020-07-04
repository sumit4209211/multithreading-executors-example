package com.techbulletinprime.threading;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskExecutor {
	public List<Future<Result>> execute(List<String> filenames) throws InterruptedException, ExecutionException {

		ExecutorService executor = Executors.newFixedThreadPool(3);
		
		/*
		  Set<Callable<String>> callables = new HashSet<Callable<String>>();

		callables.add(new Callable<String>() {
			public String call() throws Exception {
				return "Dummy Task";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				return "Another Dummy task";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				return "Different Task";
			}
		});

		List<Future<String>> futures = executorService.invokeAll(callables);
		
		*/
		List<Future<Result>> resultList = new ArrayList<>();
		for (String file : filenames) {
			Future<Result> resultfuture = executor.submit(() -> {
				Result result = null;
				try {
					File input = new File("src/sourcelocation/" + file);
					File output = new File("src/destinationlocation/" + file);
					Scanner sc = new Scanner(input);
					PrintWriter printer = new PrintWriter(output);
					while (sc.hasNextLine()) {
						String s = sc.nextLine();
						printer.write(s);
					}
					printer.flush();
					result = new Result();
					result.setStatus("Completed");

				} catch (FileNotFoundException e) {
					System.err.println("File not found. Please scan in new file.");
				}

				return result;
			});

			resultList.add(resultfuture);
		}
		return resultList;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		List<String> filelist = Arrays.asList("angular.txt", "git.txt", "node.txt");
		TaskExecutor executor = new TaskExecutor();
		List<Future<Result>> resultList = executor.execute(filelist);
		for (Future<Result> rs : resultList) {
			System.out.println(rs.get().getStatus());
		}

	}

}
