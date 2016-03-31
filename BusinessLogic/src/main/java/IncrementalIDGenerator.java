import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by HassanMahmud on 09/03/2016.
 */
public class IncrementalIDGenerator {

    private AtomicInteger termAtomicInt = new AtomicInteger(0);
    private AtomicInteger documentAtomicInt = new AtomicInteger(0);

    public int getNextTermID() {
        int increment = termAtomicInt.getAndIncrement();
        return increment;
    }

    public int getNextDocumentID() {
        int increment = documentAtomicInt.getAndIncrement();
        return increment;
    }
}