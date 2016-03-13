/**
 * Created by HassanMahmud on 04/03/2016.
 */

import dk.easv.SearchIndexer.BusinessLogicLayer.IndexTaskCallable;
import dk.easv.SearchIndexer.BusinessLogicLayer.SynchronizedTermsMap;
import dk.easv.SearchIndexer.BusinessLogicLayer.TermSplitter;
import dk.easv.SearchIndexer.DataAccessLayer.Database.ContainsRepository;
import dk.easv.SearchIndexer.DataAccessLayer.Database.DocumentsRepository;
import dk.easv.SearchIndexer.DataAccessLayer.Database.TermsRepository;
import dk.easv.SearchIndexer.DataAccessLayer.FileSystem.FileLoader;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;

public class IndexFileCallable_Tests {

    private IndexTaskCallable indexTaskCallable;

    @Before
    public void setup() {
        Path mockFilePath = Paths.get("/Users");
        SynchronizedTermsMap mockSynchronizedTermsMap = mock(SynchronizedTermsMap.class);
        FileLoader mockFileLoader = mock(FileLoader.class);
        TermSplitter mockTermSplitter = mock(TermSplitter.class);
        DocumentsRepository mockDocumentsRepo = mock(DocumentsRepository.class);
        ContainsRepository mockContainsRepo = mock(ContainsRepository.class);
        TermsRepository mockTermsRepo = mock(TermsRepository.class);

        indexTaskCallable = new IndexTaskCallable(
                mockFilePath, mockSynchronizedTermsMap, mockFileLoader, mockTermSplitter,
                mockDocumentsRepo, mockTermsRepo, mockContainsRepo
        );
    }

    @Test
    public void call__File() {


    }
}

