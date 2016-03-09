package BusinessLogicLayer;

import DataAccessLayer.Database.ContainsRepository;
import DataAccessLayer.Database.Database;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.TermsRepository;
import DataAccessLayer.FileSystem.FileLoader;
import DataAccessLayer.FileSystem.FileLoaderImpl;
import DataAccessLayer.FileSystem.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.Scanner;
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
    private static final String PATH_TO_SQL_SCRIPT = System.getProperty("user.dir") + "/DocumentTermsStructureDump.sql";

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
        if (!checkTables()) {
            initDatabase();
        }

        createRepositories();

        pool = Executors.newWorkStealingPool(DEFAULT_MAX_THREADS);
        synchronizedTermsMap = new SynchronizedTermsMap(termsRepository.readAll());
        List<Callable<String>> callables = loadFilesFromFSAndMapToCallables();
        invokeAll(callables);

        final long endTime = System.currentTimeMillis();
        System.out.print("Total execution time: " + TimeUnit.MILLISECONDS.toSeconds(endTime - startTime)
                + " seconds for " + callables.size() + " files.\n");
        shutdownAndAwaitTermination(pool);
    }

    private static boolean checkTables() {
        String sqlCheck = "SHOW TABLES";
        try (Connection connection = Database.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sqlCheck);
            ResultSet res = ps.executeQuery();
            res.last();
            if (res.getRow() != 3) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean initDatabase() {
        Statement st = null;
        try (Connection connection = Database.getConnection())
        {
            Scanner s = new Scanner(FileUtil.getInputStreamFrom(PATH_TO_SQL_SCRIPT));
            s.useDelimiter("(;(\r)?\n)|(--\n)");
            st = connection.createStatement();
            while (s.hasNext())
            {
                String line = s.next();
                if (line.startsWith("/*!") && line.endsWith("*/"))
                {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " */".length());
                }

                if (line.trim().length() > 0)
                {
                    st.execute(line);
                }
            }
            st.close();
        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
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
