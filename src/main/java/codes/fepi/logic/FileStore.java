package codes.fepi.logic;

import codes.fepi.entity.Project;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;

public class FileStore {
	private final ObjectMapper mapper = new ObjectMapper();
	private final File file = Env.getFolder().resolve("store.json").toFile();

	public void store(Collection<Project> projects) throws IOException {
		mapper.writeValue(file, projects);
	}

	public Collection<Project> read() throws IOException {
		if (!file.exists()) {
			Files.write(file.toPath(), Collections.singletonList("[]"));
		}
		return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(Collection.class, Project.class));
	}
}
