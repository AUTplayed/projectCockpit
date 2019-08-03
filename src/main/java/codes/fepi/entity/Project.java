package codes.fepi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Project {
	private String name;
	private String gitUrl;
	private int port;
	private String buildCmd;
	private String startCmd;
	private Status status;
	private boolean active;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getBuildCmd() {
		return buildCmd;
	}

	public void setBuildCmd(String buildCmd) {
		this.buildCmd = buildCmd;
	}

	public String getStartCmd() {
		return startCmd;
	}

	public void setStartCmd(String startCmd) {
		this.startCmd = startCmd;
	}

	public String getGitUrl() {
		return gitUrl;
	}

	public void setGitUrl(String gitUrl) {
		this.gitUrl = gitUrl;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@JsonIgnore
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@JsonIgnore
	public String getGitProjectName() {
		String[] split = gitUrl.split("/");
		return split[split.length - 1];
	}
}
