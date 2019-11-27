package codes.fepi.logic;

import codes.fepi.entity.Project;
import codes.fepi.ui.Repository;

import java.io.IOException;
import java.util.Collection;

public class PeriodicWorker implements Runnable {

	private long lastSaved = 0;
	private FileStore fileStore;

	public PeriodicWorker(FileStore fileStore) {
		this.fileStore = fileStore;
	}

	@Override
	public void run() {
		Collection<Project> projects = Repository.INSTANCE.getProjects();
		for (Project project : projects) {
			ProjectManagement.updateHealth(project);
		}
		long lastChanged = Repository.INSTANCE.getLastChanged();
		if (lastChanged > lastSaved) {
			try {
				fileStore.store(projects);
				lastSaved = lastChanged;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
