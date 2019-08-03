package codes.fepi.ui;

import codes.fepi.entity.Project;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public enum Repository {

	INSTANCE;

	private long lastChanged = 0;

	private List<Project> projects;

	Repository() {
		projects = new CopyOnWriteArrayList<>();    //todo use concurrentHashMap instead?
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void init(List<Project> projects) {
		this.projects.addAll(projects);
		// add guard against double invocation
	}

	public void updateProject(Project project) {
		Project storedProject = getProjects().stream()
				.filter(p -> p.getName().equals(project.getName()))
				.findFirst()
				.orElse(null);

		// set all props
		touched();
	}

	private void touched() {
		lastChanged = System.currentTimeMillis();
	}

	public long getLastChanged() {
		return lastChanged;
	}
}
