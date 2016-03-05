package DataAccessLayer.FileSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLoaderImpl implements FileLoader {

    private static final int MAX_DIR_LVLS_TO_SEARCH = Integer.MAX_VALUE;
    private static final String GLOB_TXT_PATTERN = "glob:**/*.txt";

    @Override
    public List<File> loadFiles(Path basePath) {
        PathMatcher pathMatcher
                = FileSystems.getDefault().getPathMatcher(GLOB_TXT_PATTERN);

        try (Stream<Path> allRegularFilesStream
                = Files.find(basePath, MAX_DIR_LVLS_TO_SEARCH,
                        (Path path, BasicFileAttributes bfa)
                        -> pathMatcher.matches(path))) {

            List<File> allFiles = allRegularFilesStream
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            return allFiles;

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<File> loadFilesWithFindFileVisitor(Path basePath) {
        try {
            FindFileVisitor findFileVisitor
                    = new FindFileVisitor(GLOB_TXT_PATTERN);

            Files.walkFileTree(basePath, findFileVisitor);

            return findFileVisitor.getFiles();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<String> loadLines(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(
                filePath, Charset.forName("ISO-8859-1"))) {

            return reader.lines().collect(Collectors.toList());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
