package codes.fepi.ui;

import codes.fepi.entity.Project;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public enum Repository {

	INSTANCE;

	private long lastChanged = 0;
	private long idAutoIncrement = -1;

	private ConcurrentHashMap<Long, Project> projects;

	Repository() {
		projects = new ConcurrentHashMap<>();
	}

	public Collection<Project> getProjects() {
		return projects.values();
	}

	public void init(Collection<Project> projects) {
		if (idAutoIncrement != -1) {
			throw new RuntimeException("Repository initialized more than once");
		}
		long idMax = 0;
		for (Project project : projects) {
			idMax = Math.max(idMax, project.getId());
			this.projects.put(project.getId(), project);
		}
		idAutoIncrement = idMax + 1;
	}

	public void upsertProject(Project project) {
		if (project.getId() == Project.UNSET_ID) {
			synchronized (this) {
				project.setId(idAutoIncrement);
				idAutoIncrement++;
			}
			projects.put(project.getId(), project);
		} else {
			Project storedProject = projects.get(project.getId());
			storedProject.setName(project.getName());
			storedProject.setPort(project.getPort());
			storedProject.setActive(project.isActive());
			storedProject.setStartCmd(project.getStartCmd());
			storedProject.setBuildCmd(project.getBuildCmd());
			storedProject.setGitUrl(project.getGitUrl());
		}
		touched();
	}

	private void touched() {
		lastChanged = System.currentTimeMillis();
	}

	public long getLastChanged() {
		return lastChanged;
	}

	public Project getProjectById(Long id) {
		return projects.get(id);
	}
}
