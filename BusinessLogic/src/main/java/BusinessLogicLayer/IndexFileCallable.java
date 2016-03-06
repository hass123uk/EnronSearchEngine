package BusinessLogicLayer;

import DataAccessLayer.Database.ContainsRepository;
import DataAccessLayer.Database.DocumentsRepository;
import DataAccessLayer.Database.TermsRepository;
import DataAccessLayer.FileSystem.FileLoader;
import com.enron.search.domainmodels.Document;
import com.enron.search.domainmodels.Term;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class IndexFileCallable implements Callable {

    private final FileLoader fileLoader;
    private final TermSplitter termSplitter;
    private final DocumentsRepository documentsRepository;
    private final TermsRepository termsRepository;
    private final ContainsRepository containsRepository;
    private final Path filePath;

    public IndexFileCallable(
            Path filePath,
            FileLoader fileLoader,
            TermSplitter termSplitter,
            DocumentsRepository documentsRepository,
            TermsRepository termsRepository,
            ContainsRepository containsRepositroy) {

        this.filePath = filePath;
        this.fileLoader = fileLoader;
        this.termSplitter = termSplitter;
        this.documentsRepository = documentsRepository;
        this.termsRepository = termsRepository;
        this.containsRepository = containsRepositroy;

    }

    @Override
    public Object call() throws Exception {
        List<String> lines = fileLoader.loadLines(filePath);

        Date indexTime = new Date();
        List<Term> terms = termSplitter.splitLines(lines);
        int documentId = saveDocument(filePath.toAbsolutePath().toString(), indexTime);
        if (documentId != -1) {
            List<Integer> termIDs = saveTerms(terms);
            saveRelation(documentId, termIDs);
            return "Success - Document inserted in the database! FilePath: " + filePath.toString() + ".\n"
                    + "TermIds Size: " + termIDs.size() + " - Terms Size: " + terms.size() + ".\n";
        }
        return "Error - Document was not inserted in the database! File Path: " + filePath.toString() + ".";
    }

    private int saveDocument(String absoluteFilePath, Date indexTime) {
        Document document = new Document(absoluteFilePath, indexTime);
        try {
            return documentsRepository.saveDocument(document);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private List<Integer> saveTerms(List<Term> terms) {
        for (Term term : terms) {
            //Check here for term duplicates.
            try {
                int termId = termsRepository.saveTerm(term.getTerm_Value());
                term.setTerm_ID(termId);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return terms.stream().map(Term::getTerm_ID).collect(Collectors.toList());
    }

    private void saveRelation(int documentId, List<Integer> termIDs) {
        for (Integer termId : termIDs) {
            if(termId != -1) {
                containsRepository.saveIndexInContainTbl(termId, documentId, termIDs.indexOf(termId));
            }
        }
    }
}
