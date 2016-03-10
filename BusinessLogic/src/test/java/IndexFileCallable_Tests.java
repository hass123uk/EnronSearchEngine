/**
 * Created by HassanMahmud on 04/03/2016.
 */

import BusinessLogicLayer.IndexTaskCallable;
import BusinessLogicLayer.TermSplitter;
import BusinessLogicLayer.SynchronizedTermsMap;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.ContainsRepository;
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

    private void createMocks() {
        mockSynchronizedTermsMap = mock(SynchronizedTermsMap.class);

        mockFileLoader = mock(FileLoader.class);
        mockTermSplitter = mock(TermSplitter.class);

        mockDocumentsRepo = mock(DocumentsRepository.class);
        mockContainsRepo = mock(ContainsRepository.class);
        mockTermsRepo = mock(TermsRepository.class);
    }

    @Before
    public void setup() {
        Path filePath = Paths.get("/Users/HassanMahmud/test");

        termList = new ArrayList<>();
        for (int i = 0; i < NUM_OF_TERMS; i++) {
            termList.add(new Term("test" + i));
        }

        indexTaskCallable = new IndexTaskCallable(
                filePath, mockSynchronizedTermsMap, mockFileLoader, mockTermSplitter,
                mockDocumentsRepo, mockTermsRepo, mockContainsRepo
        );
    }

    @Test
    public void saveTerms_TestAgainstManyTerms_AssertNoDuplicateTermIDs() {

        List<String> termsIDs = indexTaskCallable.saveTerms(termList);
        assertNotNull(termsIDs);
    }
}

