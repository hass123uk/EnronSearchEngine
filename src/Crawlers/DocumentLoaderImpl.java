package Crawlers;

import BE.Document;
import BE.Term;
import BLL.DocumentLoader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentLoaderImpl implements DocumentLoader {

    private ArrayList<Document> allDocuments;
    private ArrayList<Document> allDocumentsWithTerms;

    @Override
    public List<Document> loadAllDocuments(String baseFilePath)
            throws IOException {
        allDocuments = new ArrayList<>();

        recursiveFileLoad(Paths.get(baseFilePath).toFile(), false);
        return allDocuments;
    }

    @Override
    public List<Document> loadAllDocumentsWithTerms(String baseFilePath)
            throws IOException {
        allDocumentsWithTerms = new ArrayList<>();

        recursiveFileLoad(Paths.get(baseFilePath).toFile(), true);
        return allDocumentsWithTerms;
    }

    private void recursiveFileLoad(File file, boolean withTerms) {
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            for (File subFile : subFiles) {
                recursiveFileLoad(subFile, withTerms);
            }

        } else if (file.isFile() && !file.isHidden()) {
            if (!withTerms) {
                processFile(file);
            } else {
                processFileWithTerms(file);
            }
        }
    }

    private void processFile(File file) {
        allDocuments.add(new Document(file.getAbsolutePath()));
    }

    private void processFileWithTerms(File file) {
        Path path = file.toPath();
        List<Term> allTerms = new ArrayList<>();

        try (BufferedReader reader
                = Files.newBufferedReader(path, Charset.defaultCharset())) {

            reader.lines().forEachOrdered(line -> {
                String[] lineSplitBySpace = line.split("\\s");
                for (String term : lineSplitBySpace) {
                    Term nextTerm = new Term(term);
                    allTerms.add(nextTerm);
                }
            });

            allDocumentsWithTerms.add(new Document(file.getAbsolutePath(),
                    new Date(System.currentTimeMillis()), allTerms));
        } catch (IOException ex) {

        }

    }

//    private void loadAllDirPaths(Path start) throws IOException {
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(start)) {
//            for (Path entry : stream) {
//                File file = entry.toFile();
//                if (file.isDirectory()) {
//                    allDocuments.add(new Document(file.getAbsolutePath()));
//                    for (File f : file.listFiles()) {
//                        loadAllDirPaths(f.toPath());
//                    }
//                }
//            }
//        } catch (DirectoryIteratorException ex) {
//            // I/O error encounted during the iteration, the cause is an IOException
//            System.out.println(ex.getMessage());
//        }
//    }
//
//    private void crawl(File f) {
//        Date start = new Date();
//
//        crawl(f, "");
//        documentRepo.createDocuments(documents);
//        Date end = new Date();
//        long timeSpan = (end.getTime() - start.getTime()) / 1000;
//    }
//    }
//    public Map<Integer, Term> readFile(String path) {
//        try (Stream<String> lines = Files.lines(Paths.get(path), Charset.defaultCharset())) {
//            lines.forEachOrdered(line -> process(line));
//
//            List<String> collect = Files.readAllLines(Paths.get(path))
//                    .stream()
//                    .map(l -> l.split(" "))
//                    .flatMap(Arrays::stream)
//                    .collect(Collectors.toList());
//
//        } catch (IOException e) {
//        }
//    }
}
