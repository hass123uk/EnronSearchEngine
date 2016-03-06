/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccessLayer.FileSystem;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public interface FileLoader {

    public List<File> loadFiles(Path basePath);

    public List<Path> loadPaths(Path basePath);

    public List<Path> loadUsingDirStream(Path basePath);

    public List<File> loadFilesWithFindFileVisitor(Path basePath);

    public List<String> loadLines(Path filePath);
}
