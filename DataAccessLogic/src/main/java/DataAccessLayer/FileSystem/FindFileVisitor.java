package DataAccessLayer.FileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FindFileVisitor extends SimpleFileVisitor<Path> {

    private final PathMatcher pathMatcher;
    private final List<File> filesFound;

    public FindFileVisitor(String pathPattern) {
        filesFound = new ArrayList<>();
        pathMatcher = FileSystems.getDefault().getPathMatcher(pathPattern);
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs)
            throws IOException {
        if (pathMatcher.matches(path)) {
            filesFound.add(path.toFile());
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc)
            throws IOException {
        return FileVisitResult.CONTINUE;
    }

    public List<File> getFiles() {
        return filesFound;
    }
}
