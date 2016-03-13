package dk.easv.SearchEngine.BusinessLogicLayer;

import dk.easv.SearchEngine.DomainModels.Term;

import java.util.List;

public interface TermSplitter {

    List<Term> splitLines(List<String> lines);

}
