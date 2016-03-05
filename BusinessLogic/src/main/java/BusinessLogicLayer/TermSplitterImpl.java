package BusinessLogicLayer;

import com.enron.search.domainmodels.Term;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TermSplitterImpl implements TermSplitter {

    private final String regexToSplitOn;

    public TermSplitterImpl(String regexToSplitOn) {
        this.regexToSplitOn = regexToSplitOn;
    }

    @Override
    public List<Term> splitLines(List<String> lines) {
        return lines.stream()
                .flatMap(line -> Stream.of(line.split(regexToSplitOn)))
                .map(Term::new)
                .collect(Collectors.toList());
    }
}
