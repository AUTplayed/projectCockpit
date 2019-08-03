package codes.fepi.logic;

import codes.fepi.entity.Health;
import codes.fepi.entity.LogType;
import codes.fepi.entity.Project;
import codes.fepi.entity.Status;

import java.nio.file.Path;
import java.util.Collection;

public class ProjectManagement {

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
		Command.executeCommand(Env.getProjectFolder(project).toFile(), wrapToLog(project, project.getBuildCmd(), LogType.BUILD));
		restart(project);
	}

	public static void restart(Project project) throws Exception {
		Health health = updateHealth(project);
		if (health == Health.UP) {
			stop(project);
		}
		Command.executeCommand(Env.getProjectFolder(project).toFile(), wrapToLog(project, project.getStartCmd(), LogType.RUN));
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
			boolean running = false;
			try {
				running = Command.executeCommandContains(portAndProt, "fuser", portAndProt);
				health = Health.determine(project.isActive(), running);
			} catch (Exception ignored) {
			}
		}
		if(health == null) {
			health = Health.WTF;
		}
		project.getStatus().setHealth(health);
		return health;
	}

	private static String[] wrapToLog(Project project, String cmd, LogType logType) {
		Path logFile = Env.getProjectFolder(project).resolve(logType.getFilename());
		cmd += " >" + logFile.toAbsolutePath().toString() + " 2>&1 &";
		return cmd.split(" ");
	}
}
