package BLL;

import BE.Document;
import DAL.DocumentsRepositoryImpl;
import DAL.IDocumentsRepository;
import java.io.File;
import java.util.Date;

public class FileCrawler {

    private final IDocumentsRepository documentRepo;

    public FileCrawler(String dirToCrawl) {
        documentRepo = new DocumentsRepositoryImpl();

        File f = new File(dirToCrawl);
        crawl(f);
    }

    private void crawl(File f) {
        Date start = new Date();

        crawl(f, "");
        Date end = new Date();
        long timeSpan = (end.getTime() - start.getTime()) / 1000;
    }

    private void crawl(File f, String indent) {
        if (f.isDirectory()) {
            File[] subFiles = f.listFiles();
            indent += "    ";
            for (int i = 0; i < subFiles.length; i++) {
                crawl(subFiles[i], indent);
            }

        } else if (f.isFile() && !f.isHidden()) {
            String filepath = f.getAbsolutePath();
            documentRepo.createDocument(new Document(-1, filepath, new java.util.Date(System.currentTimeMillis())));
        }
    }

//
//    public List<String> readFileIntoListOfWords(String path) {
//        try {
//            return Files.readAllLines(Paths.get(path))
//                    .stream()
//                    .map(l -> l.split(" "))
//                    .flatMap(Arrays::stream)
//                    .collect(Collectors.toList());
//        } catch (IOException e) {
//        }
//        return Collections.emptyList();
//    }
}
