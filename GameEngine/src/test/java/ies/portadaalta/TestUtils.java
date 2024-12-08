package ies.portadaalta;

import java.io.File;

public class TestUtils {

    public TestUtils() {}

    public File getFileFromResources(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        return file;
    }


}
