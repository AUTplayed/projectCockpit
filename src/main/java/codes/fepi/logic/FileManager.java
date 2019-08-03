package codes.fepi.logic;

import codes.fepi.entity.LogType;
import codes.fepi.entity.Project;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FileManager {

	private static final long LOG_FILE_MAX_SIZE = 1000000;

	public static String getLogs(Project project, LogType logType) throws IOException {
		File file = logType.getLogFile(project);
		RandomAccessFile logFile = new RandomAccessFile(file, "r");
		long fileLength = logFile.length();
		if (fileLength > LOG_FILE_MAX_SIZE) {
			logFile.seek(fileLength - LOG_FILE_MAX_SIZE);
		}
		StringBuilder logBuilder = new StringBuilder();
		logBuilder.append("Last modified: ");
		logBuilder.append(Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_INSTANT));
		logBuilder.append("\n");
		String line;
		while ((line = logFile.readLine()) != null) {
			logBuilder.append(line);
			logBuilder.append("\n");
		}
		return logBuilder.toString();
	}
}
