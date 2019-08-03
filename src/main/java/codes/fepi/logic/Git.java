package codes.fepi.logic;

import codes.fepi.entity.Project;

import java.nio.file.Path;

class Git {
	/**
	 * initializes repository if directory isn't one yet
	 */
	static void init(Project project) throws Exception {
		Path dir = Env.getProjectFolder(project.getName()).toAbsolutePath();
		boolean existing = Command.executeCommandContains("existing", "git", "-C", dir.toString(), "init");
		if (!existing) {
			Command.executeCommand("git", "-C", dir.toString(), "remote", "add", "origin", project.getGitUrl());
		}
	}

	static void update(Project project) throws Exception {
		Path dir = Env.getProjectFolder(project.getName()).toAbsolutePath();
		Command.executeCommand("git", "-C", dir.toString(), "fetch");
		Command.executeCommand("git", "-C", dir.toString(), "reset", "--hard", "origin/master");
	}
}
