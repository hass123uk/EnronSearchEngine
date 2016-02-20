package DataAccessTests;

import DataAccessLayer.FileSystem.FileLoader;
import DataAccessLayer.FileSystem.FileLoaderImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileLoaderTest {

    public FileLoader fileLoader;

    private static final String HOME_DIR = "/Users/HassanMahmud/EnronDataSet";
    private static final String FILE_NAME = "/Junit_EnronMock";
    private static final String TEST_DIR = HOME_DIR + FILE_NAME;

    @Before
    public void setUp() {
        fileLoader = new FileLoaderImpl();
    }

    @Test
    public void AssertFilesListNotNull() throws IOException {
        Assert.assertNotNull(fileLoader.loadFiles(Paths.get(TEST_DIR)));
    }

    @Test(expected = IOException.class)
    public void AssertFirstFileNotDirectories() throws IOException {
        //Setup
        String pathToTest = TEST_DIR + "/EmptyFolder";
        File fileZero = fileLoader.loadFiles(Paths.get(pathToTest)).get(0);

        //Act
        Assert.assertEquals(
                fileZero.isDirectory(),
                Boolean.FALSE
        );
    }

}
