package Interface;

import org.apache.commons.cli.*;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;

import static BusinessLogicLayer.EnronSearchEngine.run;

/**
 * Created by anthonymonori on 13/03/16.
 */
public class MainApplication {

    public static void main(String[] args) {
        // setting up configuration
        File f = null;
        boolean notInitialized = false;
        try {
            f = new File("config.properties");
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


        // create Options object
        Options options = new Options();

        // add t option
        options.addOption(new Option("help", false, "Prints this message"));
        options.addOption(new Option("dbtype", true, "Set database type, eg. mysql"));
        options.addOption(new Option("dbhost", true, "Set database hostname, eg. localhost"));
        options.addOption(new Option("dbname", true, "Set database name"));
        options.addOption(new Option("dbuser", true, "Set the username for the database connection"));
        options.addOption(new Option("dbpass", true, "Set the password for the database connection"));
        options.addOption(new Option("showsettings", false, "Print current settings"));
        options.addOption(new Option("run", true, "Run the indexing with a given path"));

        // create the parser
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(options, args);
            // these can only be called by themselves
            if(line.hasOption("help")) {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "search-indexer", options );

                return;
            }
            if (line.hasOption("showsettings")) {
                System.out.println("DB_TYPE="+(config.containsKey("DB_TYPE") ? config.getString("DB_TYPE") : ""));
                System.out.println("DB_URL="+(config.containsKey("DB_URL") ? config.getString("DB_URL") : ""));
                System.out.println("DB_USER="+(config.containsKey("DB_USER") ? config.getString("DB_USER") : ""));
                System.out.println("DB_PASSWORD="+(config.containsKey("DB_PASS") ? config.getString("DB_PASS") : ""));

                System.out.println("ID_DEFAULT_FOLDER_TO_MONITOR="+ (config.containsKey("ID_DEFAULT_FOLDER_TO_MONITOR") ? config.getString("ID_DEFAULT_FOLDER_TO_MONITOR") : ""));
                System.out.println("ID_EXTENSION="+(config.containsKey("ID_EXTENSION") ? config.getString("ID_EXTENSION") : ""));
                return;
            }
            if (line.hasOption("run")) {
                String path = line.getOptionValue("run");
                config.setProperty("ID_DEFAULT_FOLDER_TO_MONITOR", path);
                System.out.println("Starting indexing: "+path);
                try {
                    run(config);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

            // these can be called at the same time
            if(line.hasOption("dbtype")) {
                System.out.println("Setting the database type...");
                switch (line.getOptionValue("dbtype")) {
                    case "mysql":
                        config.setProperty("DB_TYPE", "mysql");
                        break;
                    default:
                        System.out.println("The specified database type "+line.getOptionValue("dbtype")+" is unknown.");
                }
            }
            if(line.hasOption("dbhost")) {
                if (config != null) {
                    System.out.println("Setting the database hostname...");
                    config.setProperty("DB_HOSTNAME", line.getOptionValue("dbhost"));
                }
            }
            if(line.hasOption("dbname")) {
                if (config != null) {
                    System.out.println("Setting the database name...");
                    config.setProperty("DB_NAME", line.getOptionValue("dbname"));
                }
            }
            if(line.hasOption("dbuser")) {
                if (config != null) {
                    System.out.println("Setting the database username...");
                    config.setProperty("DB_USER", line.getOptionValue("dbuser"));
                }
            }
            if(line.hasOption("dbpass")) {
                if (config != null) {
                    System.out.println("Setting the database password...");
                    config.setProperty("DB_PASS", line.getOptionValue("dbpass"));
                }
            }
        } catch( ParseException exp ) {
            System.out.println( "Unexpected exception:" + exp.getMessage() );
        }
    }

}
