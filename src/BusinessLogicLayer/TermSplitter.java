package BusinessLogicLayer;

import BusinessEntities.Term;
import java.util.List;

public interface TermSplitter {

    List<Term> splitLines(List<String> lines);

}
