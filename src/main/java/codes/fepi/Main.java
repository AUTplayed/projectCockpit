package codes.fepi;

import codes.fepi.entity.Project;
import codes.fepi.ldfspark.LdfSpark;
import codes.fepi.logic.Env;
import codes.fepi.logic.FileStore;
import codes.fepi.logic.PeriodicWorker;
import codes.fepi.logic.ProjectManagement;
import codes.fepi.ui.ApiHandler;
import codes.fepi.ui.Repository;
import spark.Spark;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) throws Exception {
		if (Env.windows) {
			System.out.println("Windows detected, process status/restart feature unavailable");
		}
		Spark.port(getPort(args));
		LdfSpark.start();
		ApiHandler.init();

		FileStore fileStore = new FileStore();
		Repository.INSTANCE.init(fileStore.read());
		ProjectManagement.initAll(Repository.INSTANCE.getProjects());

		PeriodicWorker periodicWorker = new PeriodicWorker(fileStore);
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(periodicWorker, 1, 10, TimeUnit.MINUTES);
	}

	private static int getPort(String[] args) {
		String envPort = System.getenv("PORT");
		if (envPort != null) {
			return Integer.valueOf(envPort);
		}
		for (String arg : args) {
			if (arg.matches("\\d+")) {
				return Integer.valueOf(arg);
			}
		}
		return 6066;
	}
}
