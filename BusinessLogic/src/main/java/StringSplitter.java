import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringSplitter {

    private final String regexToSplitOn;

    public StringSplitter(String regexToSplitOn) {
        this.regexToSplitOn = regexToSplitOn;
    }

    public List<String> split(List<String> lines) {
        return lines.stream()
                .flatMap(line -> Stream.of(line.split(regexToSplitOn)))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
