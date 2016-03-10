package BusinessLogicLayer;

import com.enron.search.domainmodels.Term;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by HassanMahmud on 05/03/2016.
 */
public class SynchronizedTermsMap {
    private IncrementalIDGenerator incrementalIDGenerator;
    private Map<String, String> syncTermsMap;

    public SynchronizedTermsMap(List<Term> terms, IncrementalIDGenerator incrementalIDGenerator) {
        this.incrementalIDGenerator = incrementalIDGenerator;
        Map<String, String> termsMap = terms.stream().collect(
                Collectors.toMap(Term::getTerm_Value, Term::getTerm_ID));
        syncTermsMap = Collections.synchronizedMap(termsMap);
    }

    public synchronized int getMapSize(){
        return syncTermsMap.size();
    }
//
//    public synchronized String checkDuplicateTerms(String term_value, String generatedTermId) {
//        boolean containsTermValue = syncTermsMap.containsKey(term_value);
//        if (containsTermValue) {
//            return syncTermsMap.get(term_value);
//        } else {
//            syncTermsMap.put(term_value, generatedTermId);
//            return generatedTermId;
//        }
//    }

    public synchronized String getIdOrGenerateNewId(String term_value) {
        boolean containsTermValue = syncTermsMap.containsKey(term_value);
        if (containsTermValue) {
            return syncTermsMap.get(term_value);
        } else {
            String newId = incrementalIDGenerator.termIdGenerator();
            syncTermsMap.put(term_value, newId);
            return newId;
        }
    }
}
