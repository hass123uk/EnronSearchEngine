package BusinessLogicLayer;

import DataAccessLayer.Database.ContainsRepository;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.TermsRepository;
import DataAccessLayer.FileSystem.FileLoader;
import DataAccessLayer.FileSystem.FileLoaderImpl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 *
 */
public class EnronSearchEngine {

    private static final String HOME_DIR = "~";
    private static final String FILE_NAME = "/EnronDataSet";

    private static final String ALL_DOCS = "/MailDir_FullSet";
    private static final String HALF_ALL_DOCS = "/MailDir_HalfSet";
    private static final String FEW_DOCS = "/MailDir_SubSet";

    private static final int DEFAULT_MAX_THREADS = 8;

    private static final String ENRON_DATASET_DIR
            = HOME_DIR
            + FILE_NAME
            + FEW_DOCS;

    private static FileLoader loader;
    private static ExecutorService pool;
    private static TermsRepository termsRepository;

    public static void main(String[] args) throws Exception {
        final long startTime = System.currentTimeMillis();

        loader = new FileLoaderImpl();
        termsRepository = new TermsRepository();
        TermSplitter splitter = new TermSplitterImpl("\\s+");

        int maxThreads = DEFAULT_MAX_THREADS;

        pool = Executors.newWorkStealingPool();

        Collection<Callable<String>> indexFileCallableList = new ArrayList<>();

        List<File> files = loader.loadFiles(Paths.get(ENRON_DATASET_DIR.replaceFirst("^~",System.getProperty("user.home"))));

        for (File file : files) {
            IndexFileCallable indexFileCallable = createIndexFileRunnable(file.toPath(), loader, splitter);
            indexFileCallableList.add(indexFileCallable);
        }
        files.clear();

        List<String> threadResults = invokeAllCallablesAndWait(indexFileCallableList);
        final long endTime = System.currentTimeMillis();
        System.out.print("Total execution time: " + TimeUnit.MILLISECONDS.toSeconds(endTime - startTime)+ "for "+indexFileCallableList.size()+" files.");
//        threadResults.stream().forEach(System.out::println);
        shutdownAndAwaitTermination(pool);
    }

    private static List<String> invokeAllCallablesAndWait(Collection<Callable<String>> indexFileCallableList) {
        try {
            return pool.invokeAll(indexFileCallableList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    }).collect(Collectors.toList());

        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IndexFileCallable createIndexFileRunnable(Path filePath, FileLoader loader, TermSplitter splitter) {
        DocumentsRepository documentsRepository = new DocumentsRepository();
        ContainsRepository containsRepository = new ContainsRepository();

        return new IndexFileCallable(
                filePath, loader, splitter,
                documentsRepository, termsRepository, containsRepository
        );
    }

    private static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
