package BusinessLogicLayer;

import com.enron.search.domainmodels.Term;
import java.util.List;

public interface TermSplitter {

    List<Term> splitLines(List<String> lines);

}
