package codes.fepi.ui;

import codes.fepi.dto.ProjectOverviewDto;
import codes.fepi.entity.Project;
import codes.fepi.logic.ProjectManagement;
import spark.Request;

@codes.fepi.ldfspark.PageHandler
public class PageHandler {
	public Object index() {
		return Repository.INSTANCE.getProjects().stream().map(ProjectOverviewDto::new).toArray();
	}

	public Object project(Request req) {
		String idString = req.queryParams("id");
		if(idString == null) {
			return new Project();
		}
		Long id = Long.valueOf(idString);
		Project project = Repository.INSTANCE.getProjectById(id);
		ProjectManagement.updateHealth(project);
		return project;
	}
}
