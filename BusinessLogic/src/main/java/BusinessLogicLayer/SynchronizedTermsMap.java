package BusinessLogicLayer;

import DomainModels.Term;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by HassanMahmud on 05/03/2016.
 */
public class SynchronizedTermsMap {

    public static final int TERM_NOT_PRESENT = -1;
    public Lock lock = new ReentrantLock();
    private Map<String, Integer> syncTermsMap;

    public SynchronizedTermsMap(List<Term> terms) {
        Map<String, Integer> termsMap = terms.stream().collect(
                Collectors.toMap(Term::getTerm_Value, Term::getTerm_ID));
        syncTermsMap = Collections.synchronizedMap(termsMap);
    }

    public synchronized int getMapSize() {
        return syncTermsMap.size();
    }

    public synchronized int getTermIDIfPresent(String term_value) {
        boolean containsTermValue = syncTermsMap.containsKey(term_value);
        if (containsTermValue) {
            return syncTermsMap.get(term_value);
        } else {
            return TERM_NOT_PRESENT;
        }
    }

    public synchronized void putTerm(String term_value, int term_id) {
        syncTermsMap.put(term_value, term_id);
    }
}
