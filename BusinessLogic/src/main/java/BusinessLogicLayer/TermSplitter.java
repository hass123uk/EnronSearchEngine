package BusinessLogicLayer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TermSplitter {

    private final String regexToSplitOn;

    public TermSplitter(String regexToSplitOn) {
        this.regexToSplitOn = regexToSplitOn;
    }

    public List<String> splitLines(List<String> lines) {
        return lines.stream()
                .flatMap(line -> Stream.of(line.split(regexToSplitOn)))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
