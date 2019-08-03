package codes.fepi.logic;

import codes.fepi.entity.Project;

import java.nio.file.Path;

class Git {
	/**
	 * initializes repository if directory isn't one yet
	 */
	static void init(Project project) throws Exception {
		Path dir = Env.getProjectFolder(project).toAbsolutePath();
		boolean existing = Command.executeCommandContains(dir.toFile(), "existing", "git", "init");
		if (!existing) {
			Command.executeCommand(dir.toFile(), "git", "remote", "add", "origin", project.getGitUrl());
		}
	}

	static void update(Project project) throws Exception {
		Path dir = Env.getProjectFolder(project).toAbsolutePath();
		Command.executeCommand(dir.toFile(), "git", "fetch");
		Command.executeCommand(dir.toFile(), "git", "reset", "--hard", "origin/master");
	}
}
