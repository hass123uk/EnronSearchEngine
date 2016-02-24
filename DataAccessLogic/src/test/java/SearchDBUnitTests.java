
import DataAccessLayer.Database.SearchDB;
import com.google.common.collect.Multimap;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SearchDBUnitTests {

    public SearchDBUnitTests() {
    }

    @Before
    public void setUp() {

    }

    @Test
    public void hello() {
        SearchDB searchDB = new SearchDB();
        Multimap<String, String> term_DocPath
                = searchDB.getSimilarTermsWithDocumentPath("Enron");

        Collection<String> listOfDocPaths = term_DocPath.get("phillip.allen@enron.com");

        Assert.assertEquals(2038, listOfDocPaths.size());
    }
}
