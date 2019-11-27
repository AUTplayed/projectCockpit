package codes.fepi.logic;

import codes.fepi.entity.Health;
import codes.fepi.entity.LogType;
import codes.fepi.entity.Project;
import codes.fepi.entity.Status;
import codes.fepi.ui.Repository;

import java.io.IOException;
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
		Nginx.registerSubdomain(project);
	}

	public static void updateAndRestart(Project project) throws Exception {
		if (!project.getGitUrl().contains("AUTplayed/projectCockpit")) {
			Git.update(project);
			Command.executeCommand(Env.getProjectFolder(project).toFile(),
					LogType.BUILD.getLogFile(project),
					true,
					toArgs(project.getBuildCmd()));
			restart(project);
		} else {
			final Path selfPath = Env.getFolder().getParent().getParent();
			Git.updateSelf();
			Command.executeCommand(selfPath.toFile(),
					LogType.BUILD.getLogFile(project),
					true,
					toArgs(project.getBuildCmd()));
			restartSelf(project, selfPath);
		}
	}

	private static void restartSelf(Project project, Path selfPath) throws Exception {
		new FileStore().store(Repository.INSTANCE.getProjects());
		Command.executeCommand(selfPath.toFile(),
				LogType.RUN.getLogFile(project),
				false,
				toArgs(project.getStartCmd()));
		Command.executeCommand(null, false, "fuser", "-k", project.getPort() + "/tcp");
	}

	private static void restart(Project project) throws Exception {
		Health health = updateHealth(project);
		if (health == Health.UP) {
			stop(project);
		}
		Command.executeCommand(Env.getProjectFolder(project).toFile(),
				LogType.RUN.getLogFile(project),
				false,
				toArgs(project.getStartCmd()));
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
				Command.executeCommand("fuser", portAndProt);
				running = true;
			} catch (Exception ignored) {
				// fuser exits with code 1 if it doesn't find anything...
				running = false;
			}
			health = Health.determine(project.isActive(), running);
		}
		if (health == null) {
			health = Health.WTF;
		}
		project.getStatus().setHealth(health);
		return health;
	}

	private static String[] toArgs(String cmd) {
		return cmd.split(" ");
	}
}
