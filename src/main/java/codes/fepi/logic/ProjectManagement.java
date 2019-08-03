package codes.fepi.logic;

import codes.fepi.entity.Health;
import codes.fepi.entity.LogType;
import codes.fepi.entity.Project;
import codes.fepi.entity.Status;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class ProjectManagement {

	private static final long LOG_FILE_MAX_SIZE = 1000000;

	public static void initAll(Collection<Project> projects) {
		for (Project project : projects) {
			try {
				initProject(project);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void initProject(Project project) throws Exception {
		Git.init(project);
		Status status = new Status();
		project.setStatus(status);
		updateHealth(project);
	}

	public static void updateAndRestart(Project project) throws Exception {
		Git.update(project);
		Command.executeCommand(Env.getProjectFolder(project).toFile(),
				LogType.BUILD.getLogFile(project),
				true,
				toArgs(project.getBuildCmd()));
		restart(project);
	}

	public static void restart(Project project) throws Exception {
		Health health = updateHealth(project);
		if (health == Health.UP) {
			stop(project);
		}
		Command.executeCommand(Env.getProjectFolder(project).toFile(),
				LogType.RUN.getLogFile(project),
				false,
				toArgs(project.getStartCmd()));
		updateHealth(project);
	}

	public static void stop(Project project) throws Exception {
		if (!Env.windows) {
			String portAndProt = project.getPort() + "/tcp";
			Command.executeCommand("fuser", "-k", portAndProt);
		}
	}

	public static Health updateHealth(Project project) {
		Health health = null;
		if (!Env.windows) {
			String portAndProt = project.getPort() + "/tcp";
			boolean running;
			try {
				running = Command.executeCommandContains(portAndProt, "fuser", portAndProt);
				health = Health.determine(project.isActive(), running);
			} catch (Exception ignored) {
			}
		}
		if (health == null) {
			health = Health.WTF;
		}
		project.getStatus().setHealth(health);
		return health;
	}

	public static String getLogs(Project project, LogType logType) throws IOException {
		File file = logType.getLogFile(project);
		RandomAccessFile logFile = new RandomAccessFile(file, "r");
		long fileLength = logFile.length();
		if (fileLength > LOG_FILE_MAX_SIZE) {
			logFile.seek(fileLength - LOG_FILE_MAX_SIZE);
		}
		StringBuilder logBuilder = new StringBuilder();
		logBuilder.append("Last modified: ");
		logBuilder.append(Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_INSTANT));
		logBuilder.append("\n");
		String line;
		while ((line = logFile.readLine()) != null) {
			logBuilder.append(line);
			logBuilder.append("\n");
		}
		return logBuilder.toString();
	}

	private static String[] toArgs(String cmd) {
		return cmd.split(" ");
	}
}
