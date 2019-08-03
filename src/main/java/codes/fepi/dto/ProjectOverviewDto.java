package codes.fepi.dto;

import codes.fepi.entity.Health;
import codes.fepi.entity.Project;

public class ProjectOverviewDto {
	private String name;
	private long id;
	private Health health;

	public ProjectOverviewDto(Project project) {
		this.name = project.getName();
		this.id = project.getId();
		if (project.getStatus() == null) {
			this.health = Health.WTF;
		} else {
			this.health = project.getStatus().getHealth();
		}
	}

	public String getName() {
		return name;
	}

	public Health getHealth() {
		return health;
	}

	public long getId() {
		return id;
	}
}
