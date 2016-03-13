/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.easv.SearchIndexer.DataAccessLayer.FileSystem;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 *
 */
public interface FileLoader {

    List<File> loadFiles(Path basePath);

    List<Path> loadPaths(Path basePath);

    List<Path> loadUsingDirStream(Path basePath);

    List<File> loadFilesWithFindFileVisitor(Path basePath);

    List<String> loadLines(Path filePath);
}
