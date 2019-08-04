package codes.fepi.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

class Command {

	static boolean executeCommandContains(File workDir, String contains, String... args) throws Exception {
		AtomicBoolean contained = new AtomicBoolean(false);
		executeCommand(workDir, (s) -> {
			if (s.contains(contains)) {
				contained.set(true);
			}
		}, args);
		return contained.get();
	}

	static boolean executeCommandContains(String contains, String... args) throws Exception {
		return executeCommandContains(null, contains, args);
	}

	static void executeCommand(File workDir, String... args) throws Exception {
		executeCommand(workDir, (s) -> {
		}, args);
	}

	static void executeCommand(String... args) throws Exception {
		executeCommand(null, args);
	}

	static void executeCommand(File workDir, File outFile, boolean wait, String... args) throws IOException, InterruptedException {
		ProcessBuilder builder = prepBuilder(workDir, args);
		System.out.printf("redirecting to: %s\n", outFile.getAbsolutePath());
		builder.redirectErrorStream(true);
		builder.redirectOutput(outFile);
		Process process = builder.start();
		if (wait) {
			process.waitFor();
		}
	}

	private static void executeCommand(File workDir, Consumer<String> lineHandler, String... args) throws Exception {
		ProcessBuilder builder = prepBuilder(workDir, args);
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
		if (process.waitFor() != 0) {
			throw new Exception(String.format("%s exited with error code %d:\n%s", String.join(" ", builder.command()), process.exitValue(), errors));
		}
	}

	private static ProcessBuilder prepBuilder(File workDir, String... args) {
		ProcessBuilder builder = new ProcessBuilder(args);
		if (workDir != null) {
			builder.directory(workDir);
		}
		System.out.printf("executing: %s\n", String.join(" ", builder.command()));
		return builder;
	}
}
