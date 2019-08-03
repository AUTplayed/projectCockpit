package codes.fepi.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Command {

	public static boolean executeCommandContains(String contains, String... args) throws Exception {
		AtomicBoolean contained = new AtomicBoolean(false);
		executeCommand((s) -> {
			if(s.contains(contains)) {
				contained.set(true);
			}
		}, args);
		return contained.get();
	}

	public static void executeCommand(String... args) throws Exception {
		executeCommand((s) -> {
		}, args);
	}

	public static void executeCommand(Consumer<String> lineHandler, String... args) throws Exception {
		ProcessBuilder builder = new ProcessBuilder(args);
		System.out.printf("executing: %s\n", String.join(" ", builder.command()));
		Process process = builder.start();
		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = input.readLine()) != null) {
			lineHandler.accept(line);
		}
		input.close();
		input = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		StringBuilder errors = new StringBuilder();
		while ((line = input.readLine()) != null) {
			errors.append(line).append("\n");
		}
		input.close();
		if (process.exitValue() != 0) {
			throw new Exception(String.format("%s exited with error code %d:\n%s", String.join(" ", builder.command()), process.exitValue(), errors));
		}
	}
}
