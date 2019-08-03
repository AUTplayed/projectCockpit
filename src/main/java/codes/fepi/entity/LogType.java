package codes.fepi.entity;

import codes.fepi.logic.Env;

import java.io.File;
import java.io.IOException;

public enum LogType {
	BUILD("build.log"),
	RUN("run.log");

	private final String filename;

	LogType(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public File getLogFile(Project project) throws IOException {
		File file = Env.getProjectFolder(project).resolve(getFilename()).toFile();
		file.createNewFile();
		return file;
	}
}
