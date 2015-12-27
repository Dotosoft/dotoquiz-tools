/*
	Copyright 2015 Denis Prasetio
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

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
