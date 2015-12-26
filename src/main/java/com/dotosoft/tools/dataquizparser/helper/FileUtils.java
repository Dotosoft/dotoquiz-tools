package com.dotosoft.tools.dataquizparser.helper;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
	public static Path getPath(String rootFolder, String folder, String file) {
		Path returnPath = Paths.get(rootFolder, folder, file);
		return returnPath;
	}
}
