package BusinessLogicLayer;

import DataAccessLayer.Database.ContainsRepository;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.TermsRepository;
import DataAccessLayer.FileSystem.FileLoader;
import DataAccessLayer.FileSystem.FileLoaderImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final int DEFAULT_MAX_THREADS = 10;

    private static final String ENRON_DATASET_DIR
            = HOME_DIR
            + FILE_NAME
            + FEW_DOCS;

    private static FileLoader fileLoader;
    private static TermSplitter splitter;
    private static ExecutorService pool;

    private static TermsRepository termsRepository;
    private static DocumentsRepository documentsRepository;
    private static ContainsRepository containsRepository;
    private static SynchronizedTermsMap synchronizedTermsMap;

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        fileLoader = new FileLoaderImpl();
        splitter = new TermSplitterImpl("\\W+");
        createRepositories();
        pool = Executors.newWorkStealingPool(DEFAULT_MAX_THREADS);
        synchronizedTermsMap = new SynchronizedTermsMap(termsRepository.readAll());

        List<Callable<String>> callableList = loadFilesFromFSAndMapToCallables();
        invokeAll(callableList);

        final long endTime = System.currentTimeMillis();
        System.out.print("Total execution time: " + TimeUnit.MILLISECONDS.toSeconds(endTime - startTime)
                + " seconds for " + callableList.size() + " files.\n");
        shutdownAndAwaitTermination(pool);
    }

    private static void createRepositories() {
        termsRepository = new TermsRepository();
        documentsRepository = new DocumentsRepository();
        containsRepository = new ContainsRepository();
    }

    private static List<Callable<String>> loadFilesFromFSAndMapToCallables() {
        return fileLoader
                .loadFiles(Paths.get(ENRON_DATASET_DIR.replaceFirst("^~", System.getProperty("user.home"))))
                .stream()
                .map(file -> (Callable<String>) newIndexFileTaskCallable(file.toPath()))
                .collect(Collectors.toList());
    }

    private static IndexTaskCallable newIndexFileTaskCallable(Path filePath) {
        return new IndexTaskCallable(filePath,
                synchronizedTermsMap,fileLoader, splitter, documentsRepository, termsRepository, containsRepository);
    }

    private static List<String> invokeAll(List<Callable<String>> indexFileCallableList) {
        try {
            return pool.invokeAll(indexFileCallableList)
                    .stream()
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
