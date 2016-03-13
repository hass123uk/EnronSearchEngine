package dk.easv.SearchIndexer.BusinessLogicLayer;

import dk.easv.SearchIndexer.DataAccessLayer.Database.ContainsRepository;
import dk.easv.SearchIndexer.DataAccessLayer.Database.DocumentsRepository;
import dk.easv.SearchIndexer.DataAccessLayer.Database.TermsRepository;
import dk.easv.SearchIndexer.DataAccessLayer.FileSystem.FileLoader;
import dk.easv.SearchIndexer.DataAccessLayer.FileSystem.FileLoaderImpl;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 */
public class EnronSearchEngine {

    private static final int DEFAULT_MAX_THREADS = 10;

    private static FileLoader fileLoader;
    private static TermSplitter splitter;
    private static ExecutorService pool;

    private static TermsRepository termsRepository;
    private static DocumentsRepository documentsRepository;
    private static ContainsRepository containsRepository;
    private static SynchronizedTermsMap synchronizedTermsMap;

    private static String[] mPathToMonitor;
    private static String[] mExtension;

    public static void run(String[] args) throws Exception {
        PropertiesConfiguration config = new PropertiesConfiguration();

        try {
            config.load(new File("config.properties"));
            mPathToMonitor = config.getStringArray("ID_DEFAULT_FOLDER_TO_MONITOR");
            mExtension = config.getStringArray("ID_EXTENSION");

            long startTime = System.currentTimeMillis();
            fileLoader = new FileLoaderImpl();

            splitter = new TermSplitterImpl("\\W+");
            createRepositories();

            pool = Executors.newWorkStealingPool(DEFAULT_MAX_THREADS);
            synchronizedTermsMap = new SynchronizedTermsMap(termsRepository.readAll());
            List<Callable<String>> callables = loadFilesFromFSAndMapToCallables();
            invokeAll(callables);

            final long endTime = System.currentTimeMillis();
            System.out.print("Total execution time: " + TimeUnit.MILLISECONDS.toSeconds(endTime - startTime)
                    + " seconds for " + callables.size() + " files.\n");
            shutdownAndAwaitTermination(pool);

        } catch (ConfigurationException e) {
            System.out.print("Default config.properties file not found. Please make sure you set the database and indexing settings.");
        }
    }

    private static void createRepositories() {
        termsRepository = new TermsRepository();
        documentsRepository = new DocumentsRepository();
        containsRepository = new ContainsRepository();
    }

    private static List<Callable<String>> loadFilesFromFSAndMapToCallables() {
        return fileLoader
                .loadFiles(Paths.get(mPathToMonitor[0].replaceFirst("^~", System.getProperty("user.home"))))
                .stream()
                .map(file -> (Callable<String>) newIndexFileTaskCallable(file.toPath()))
                .collect(Collectors.toList());
    }

    private static IndexTaskCallable newIndexFileTaskCallable(Path filePath) {
        return new IndexTaskCallable(filePath,
                synchronizedTermsMap, fileLoader, splitter, documentsRepository, termsRepository, containsRepository
        );
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
