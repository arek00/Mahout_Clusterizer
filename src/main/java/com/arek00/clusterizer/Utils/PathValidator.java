package com.arek00.clusterizer.Utils;


import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;

public class PathValidator {
    public static void removePathIfExists(Path path) throws IOException {
        File file = new File(path.toString());

        if (file.exists()) {
            removeFile(file);
        }
    }

    private static void removeFile(File file) throws IOException {
        FileUtils.deleteDirectory(file);
    }

}
