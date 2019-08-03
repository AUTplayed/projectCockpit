package codes.fepi.ui;

import codes.fepi.entity.LogType;
import codes.fepi.entity.Project;
import codes.fepi.logic.ProjectManagement;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Spark;

public class ApiHandler {
	public static void init() {
		Spark.path("/api", ApiHandler::route);
	}

	private static void route() {
		ObjectMapper mapper = new ObjectMapper();
		Spark.post("/project", (req, res) -> {
			Project project = mapper.readValue(req.bodyAsBytes(), Project.class);
			Repository.INSTANCE.upsertProject(project);
			return "success";
		});
		Spark.get("/project/restart/:id", (req, res) -> {
			Project project = Repository.INSTANCE.getProjectById(Long.valueOf(req.params("id")));
			ProjectManagement.updateAndRestart(project);
			return "success";
		});
		Spark.get("/project/stop/:id", (req, res) -> {
			Project project = Repository.INSTANCE.getProjectById(Long.valueOf(req.params("id")));
			ProjectManagement.stop(project);
			return "success";
		});
		Spark.get("/project/logs/:id", (req, res) -> {
			Project project = Repository.INSTANCE.getProjectById(Long.valueOf(req.params("id")));
			String logType = req.queryParams("type");
			LogType type = LogType.valueOf(logType);
			return ProjectManagement.getLogs(project, type);
		});
	}
}
