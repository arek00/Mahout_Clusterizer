package com.arek00.clusterizer;

import org.apache.hadoop.fs.Path;
import org.junit.Test;

import static org.junit.Assert.*;


public class PathTest {

    @Test
    public void toStringTest() {
        String path = "/home/arek/articles/articles";

        Path testingPath = new Path(path);
        assertTrue(path.equals(testingPath.toString()));
        testingPath = new Path(testingPath, "documents");
        assertTrue("/home/arek/articles/articles/documents".equals(testingPath.toString()));
    }


}