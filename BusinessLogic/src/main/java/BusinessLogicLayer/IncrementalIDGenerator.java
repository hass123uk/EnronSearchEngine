package BusinessLogicLayer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by HassanMahmud on 09/03/2016.
 */
public class IncrementalIDGenerator {

    private AtomicInteger termAtomicInt = new AtomicInteger(0);
    private AtomicInteger documentAtomicInt = new AtomicInteger(0);

//        UUID randomUUID = UUID.randomUUID();
//        String randomUUIDString = randomUUID.toString().replaceAll("-", "");

    public int termIdGenerator() {
        int idGeneratorAndIncrement = termAtomicInt.getAndIncrement();
        return idGeneratorAndIncrement;
    }

    public int documentPKGenerator() {
        int idGeneratorAndIncrement = documentAtomicInt.getAndIncrement();
        return idGeneratorAndIncrement;
    }
}