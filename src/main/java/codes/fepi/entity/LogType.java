package codes.fepi.entity;

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
}
