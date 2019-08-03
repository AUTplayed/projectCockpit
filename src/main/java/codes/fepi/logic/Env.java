package codes.fepi.logic;

import codes.fepi.Main;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Env {
	private static Path folder;
	public static boolean windows;

	static {
		try {
			folder = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
			windows = System.getProperty("os.name").toLowerCase().contains("win");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static Path getFolder() {
		return folder;
	}

	public static Path getProjectFolder(String projectName) {
		Path projectFolder = folder.resolve(projectName);
		if (!projectFolder.toFile().exists()) {
			projectFolder.toFile().mkdir();
		}
		return projectFolder;
	}
}
