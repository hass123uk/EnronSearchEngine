/**
 * Created by HassanMahmud on 04/03/2016.
 */

import BusinessLogicLayer.IncrementalIDGenerator;
import BusinessLogicLayer.IndexTaskCallable;
import BusinessLogicLayer.SynchronizedTermsMap;
import BusinessLogicLayer.TermSplitter;
import DataAccessLayer.Database.ContainsRepository;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.TermsRepository;
import DataAccessLayer.FileSystem.FileLoader;
import com.enron.search.domainmodels.Term;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class IndexFileCallable_Tests {

    private static final int NUM_OF_TERMS = 800;
    private List<Term> termList;

    private IndexTaskCallable indexTaskCallable;
    private SynchronizedTermsMap mockSynchronizedTermsMap;
    private FileLoader mockFileLoader;
    private TermSplitter mockTermSplitter;
    private DocumentsRepository mockDocumentsRepo;
    private ContainsRepository mockContainsRepo;
    private TermsRepository mockTermsRepo;
    private IncrementalIDGenerator mockIncrementalIDGenerator;

    private void createMocks() {
        mockSynchronizedTermsMap = mock(SynchronizedTermsMap.class);
        mockIncrementalIDGenerator = new IncrementalIDGenerator();
        mockFileLoader = mock(FileLoader.class);
        mockTermSplitter = mock(TermSplitter.class);

        mockDocumentsRepo = mock(DocumentsRepository.class);
        mockContainsRepo = mock(ContainsRepository.class);
        mockTermsRepo = mock(TermsRepository.class);
    }

    @Before
    public void setup() {
        createMocks();
        termList = new ArrayList<>();
        for (int i = 0; i < NUM_OF_TERMS; i++) {
            termList.add(new Term("id" + i, "term" + i));
        }
    }

    @Test
    public void saveTerms_TestAgainstManyTerms_AssertNoDuplicateTermIDs() {
        Path filePath = Paths.get("/Users/HassanMahmud/test");
        SynchronizedTermsMap realMap = new SynchronizedTermsMap(termList, mockIncrementalIDGenerator);
        indexTaskCallable = new IndexTaskCallable(
                filePath, mockIncrementalIDGenerator, realMap, mockFileLoader, mockTermSplitter,
                mockDocumentsRepo, mockTermsRepo, mockContainsRepo);


        List<String> termsIDs = indexTaskCallable.saveTerms(termList);
        assertNotNull(termsIDs);
    }
}

