package Interface;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;

/**
 * Created by anthonymonori on 17/03/16.
 */
public class ConfigurationManager {

    private static ConfigurationManager mInstance = null;

    private static final String CONFIG_PATH = "config.properties";

    private ConfigurationManager() {};

    public static ConfigurationManager getInstance() {
        if (mInstance == null) {
            mInstance = new ConfigurationManager();
        }
        return mInstance;
    }

    public Configuration getConfiguration() {
        // setting up configuration
        File f = null;
        boolean notInitialized = false;
        try {
            f = new File(CONFIG_PATH);
            if(!f.exists() && !f.isDirectory())
            {
                f.createNewFile();
                notInitialized = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parameters params = new Parameters();

        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class).configure(params.fileBased().setFile(f));

        // enable auto save mode
        builder.setAutoSave(true);

        Configuration config = null;
        try {
            config = builder.getConfiguration();
            if (notInitialized) {
                config.addProperty("DB_TYPE","");
                config.addProperty("DB_HOSTNAME","");
                config.addProperty("DB_NAME","");
                config.addProperty("DB_USER","");
                config.addProperty("DB_PASS","");

                config.addProperty("ID_DEFAULT_FOLDER_TO_MONITOR","");
                config.addProperty("ID_EXTENSION",".txt");
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return config;
    }
}
