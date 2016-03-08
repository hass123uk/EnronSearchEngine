package BusinessLogicLayer;

import com.enron.search.domainmodels.Term;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

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
    public final static int TEMP_TERM_ID = -1;
    private Map<String, Integer> syncTermsMap;

    public SynchronizedTermsMap(List<Term> terms) {
        Map<String, Integer> termsMap = terms.stream().collect(Collectors.toMap(Term::getTerm_Value, Term::getTerm_ID));
        syncTermsMap = Collections.synchronizedMap(termsMap);
    }

    public synchronized int putTermInMap(Term term){
        boolean containsTermValue = syncTermsMap.containsKey(term.getTerm_Value());
        if (containsTermValue) {
            Integer termId = syncTermsMap.get(term.getTerm_Value());
            return termId;
        } else {
                syncTermsMap.put(term.getTerm_Value(), TEMP_TERM_ID);
                return TEMP_TERM_ID;
        }
    }

    public synchronized void updateTermID(Term term){
        syncTermsMap.replace(term.getTerm_Value(), TEMP_TERM_ID, term.getTerm_ID());
    }
}
