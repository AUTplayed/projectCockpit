package codes.fepi.ui;

import codes.fepi.dto.ProjectOverviewDto;
import spark.Request;

@codes.fepi.ldfspark.PageHandler
public class PageHandler {
	public Object index() {
		return Repository.INSTANCE.getProjects().stream().map(ProjectOverviewDto::new).toArray();
	}

	public Object project(Request req) {
		String projectName = req.queryParams("name");

		return null;
	}
}
