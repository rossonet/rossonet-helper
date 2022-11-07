package org.rossonet.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSystemHelper {

	public static void deleteDirectory(final File file) {
		if (Files.exists(Paths.get(file.getAbsolutePath()))) {
			for (final File subfile : file.listFiles()) {
				if (subfile.isDirectory()) {
					deleteDirectory(subfile);
				}
				subfile.delete();
			}
		}
	}

	private FileSystemHelper() {
		throw new UnsupportedOperationException("Just for static usage");

	}

}
