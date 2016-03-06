package BusinessLogicLayer;

import com.enron.search.domainmodels.Term;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by HassanMahmud on 05/03/2016.
 */
public class TermsBiMapLock {
    public final Lock lock = new ReentrantLock();
    public BiMap<Integer, String> termsBiMap;

    public TermsBiMapLock(List<Term> terms) {
        termsBiMap = Maps.synchronizedBiMap(HashBiMap.create(
                terms.stream()
                        .collect(Collectors.toMap(Term::getTerm_ID, Term::getTerm_Value))));
    }


}
