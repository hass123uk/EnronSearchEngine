package DataAccessLayer.FileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by anthonymonori on 09/03/16.
 */
public final class FileUtil {

    public static InputStream getInputStreamFrom(String pathToFile) throws FileNotFoundException {
        File file = new File(pathToFile);
        return new FileInputStream(file);
    }
}
