package com.dotosoft.tools.quizparser.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
	public static Path getPath(String rootFolder, String folder, String file) {
		Path returnPath = Paths.get(rootFolder, folder, file);
		return returnPath;
	}
	
	public static boolean moveToTrash(File fileToDelete)
    {
		com.sun.jna.platform.FileUtils fileUtils = com.sun.jna.platform.FileUtils.getInstance();
        if (fileUtils.hasTrash())
        {
            try
            {
                fileUtils.moveToTrash( new File[] { fileToDelete } );
                return true;

            } catch (IOException ioe)
            {
            }
        }

        return false;
    }
}
