package dk.easv.SearchEngine.BusinessLogicLayer;

import dk.easv.SearchEngine.DataAccessLayer.FileSystem.FileLoader;
import dk.easv.SearchEngine.DataAccessLayer.FileSystem.FileLoaderImpl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileLoaderBenchmarks {

    private final FileLoader fileLoader;

    public FileLoaderBenchmarks() {
        fileLoader = new FileLoaderImpl();
    }

    public void loadUsingWalker(String baseDirPath) {
        List<File> allFiles;
        long startTime = System.nanoTime();
        allFiles = fileLoader.loadFilesWithFindFileVisitor(Paths.get(baseDirPath));
        double executionTimeInSeconds = (System.nanoTime() - startTime) / 1E9;

        printResult("Files.walkTree()", executionTimeInSeconds, allFiles.size());
    }

    public void loadUsingFind(String baseDirPath) {
        List<File> allFiles;
        long startTime = System.nanoTime();
        allFiles = fileLoader.loadFiles(Paths.get(baseDirPath));
        double executionTimeInSeconds = (System.nanoTime() - startTime) / 1E9;

        printResult("Files.Find()", executionTimeInSeconds, allFiles.size());
    }

    public void loadUsingFindWithoutMapping(String baseDirPath) {
        List<Path> allFiles;
        long startTime = System.nanoTime();
        allFiles = fileLoader.loadPaths(Paths.get(baseDirPath));
        double executionTimeInSeconds = (System.nanoTime() - startTime) / 1E9;

        printResult("Files.FindWithoutMap()", executionTimeInSeconds, allFiles.size());
    }

    public void loadUsingDirStream(String baseDirPath) {
        List<Path> allFiles;
        long startTime = System.nanoTime();
        allFiles = fileLoader.loadUsingDirStream(Paths.get(baseDirPath));
        double executionTimeInSeconds = (System.nanoTime() - startTime) / 1E9;

        printResult("Files.FindWithoutMap()", executionTimeInSeconds, allFiles.size());
    }

    public void readLinesBenchmark(List<File> allFiles) {
        long startTime = System.nanoTime();
        allFiles.stream().forEach(file -> fileLoader.loadLines(file.toPath()));
        double executionTimeInSeconds = (System.nanoTime() - startTime) / 1E9;

        printResult("BufferedReader.lines()", executionTimeInSeconds, allFiles.size());
    }

    private void printResult(
            String methodName, double executionTimeInSeconds, int filesSize) {
        System.out.println(
                methodName + "!  "
                + "MethodExecitionTime: " + executionTimeInSeconds
                + ", Files No: " + filesSize
        );
    }
}
