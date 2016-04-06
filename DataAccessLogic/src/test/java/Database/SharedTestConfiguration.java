package Database;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.ConfigurationBuilder;

/**
 * Created by anthonymonori on 31/03/16.
 */
public class SharedTestConfiguration {

    private Configuration mConfiguration;

    private SharedTestConfiguration(){
        mConfiguration = new BaseConfiguration();
        mConfiguration.addProperty("DB_TYPE","mysql");
        mConfiguration.addProperty("DB_HOSTNAME","localhost");
        mConfiguration.addProperty("DB_NAME","IndexingTestDb");
        mConfiguration.addProperty("DB_USER","sqluser");
        mConfiguration.addProperty("DB_PASS","sqluserpw");

        mConfiguration.addProperty("ID_DEFAULT_FOLDER_TO_MONITOR","");
        mConfiguration.addProperty("ID_EXTENSION",".txt");

    };

    private static SharedTestConfiguration mInstance = null;

    public static SharedTestConfiguration getInstance() {
        if (mInstance == null) {
            mInstance = new SharedTestConfiguration();
        }
        return mInstance;
    }

    public Configuration getConfiguration() {
        return mConfiguration;
    }
}
