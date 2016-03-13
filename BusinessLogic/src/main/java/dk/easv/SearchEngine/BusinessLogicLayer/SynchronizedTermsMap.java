package dk.easv.SearchEngine.BusinessLogicLayer;

import dk.easv.SearchEngine.DomainModels.Term;

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
    public final static int TERM_NOT_IN_MAP_ERROR = -1;
    public Lock lock = new ReentrantLock();
    private Map<String, Integer> syncTermsMap;

    public SynchronizedTermsMap(List<Term> terms) {
        Map<String, Integer> termsMap = terms.stream().collect(Collectors.toMap(Term::getTerm_Value, Term::getTerm_ID));
        syncTermsMap = Collections.synchronizedMap(termsMap);
    }

    public synchronized int getTermID(String term_Value) {
        boolean containsTermValue = syncTermsMap.containsKey(term_Value);
        if (containsTermValue) {
            Integer termId = syncTermsMap.get(term_Value);
            return termId;
        } else
            return TERM_NOT_IN_MAP_ERROR;
    }

    public synchronized void putTerm(Term term) {
        syncTermsMap.put(term.getTerm_Value(), term.getTerm_ID());
    }
}
