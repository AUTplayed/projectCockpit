package codes.fepi.logic;

import codes.fepi.entity.Project;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class FileStore {
	private final ObjectMapper mapper = new ObjectMapper();
	private final File file = Env.getFolder().resolve("store.json").toFile();

	public void store(List<Project> projects) throws IOException {
		mapper.writeValue(file, projects);
	}

	public List<Project> read() throws IOException {
		if (!file.exists()) {
			Files.write(file.toPath(), Collections.singletonList("[]"));
		}
		return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Project.class));
	}
}
