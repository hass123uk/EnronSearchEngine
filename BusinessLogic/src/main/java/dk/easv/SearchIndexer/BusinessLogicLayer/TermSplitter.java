package dk.easv.SearchIndexer.BusinessLogicLayer;

import dk.easv.SearchIndexer.DomainModels.Term;

import java.util.List;

public interface TermSplitter {

    List<Term> splitLines(List<String> lines);

}
