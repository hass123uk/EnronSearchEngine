package Interface;

import org.apache.commons.cli.*;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.io.IOException;

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

        Configuration config = null;
        try {
            config = new PropertiesConfiguration(f);
            if (notInitialized) {
                config.addProperty("DB_TYPE","");
                config.addProperty("DB_URL","");
                config.addProperty("DB_USER","");
                config.addProperty("DB_PASS","");

                config.addProperty("ID_DEFAULT_FOLDER_TO_MONITOR","");
                config.addProperty("ID_EXTENSION","");
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }


        // create Options object
        Options options = new Options();

        // add t option
        options.addOption(new Option("help", false, "Prints this message"));
        options.addOption(new Option("dbtype", true, "Set database type, eg. mysql"));
        options.addOption(new Option("dburl", true, "Without any prefix, and no username, password"));
        options.addOption(new Option("dbusername", true, "Set the username for the database connection"));
        options.addOption(new Option("dbpassword", true, "Set the password for the database connection"));
        options.addOption(new Option("showsettings", false, "Print current settings"));

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
                System.out.println();
            }

            // these can be called at the same time
            if(line.hasOption("dbtype")) {
                switch (line.getOptionValue("dbtype")) {
                    case "mysql":
                        config.setProperty("DB_TYPE", "mysql");
                        break;
                    default:
                        System.out.println("The specified database type "+line.getOptionValue("dbtype")+" is unknown.");
                }
            }
            if(line.hasOption("dburl")) {
                config.setProperty("DB_URL", line.getOptionValue("dburl"));
            }
            if(line.hasOption("dbusername")) {
                config.setProperty("DB_USER", line.getOptionValue("dbusername"));
            }
            if(line.hasOption("dbpassword")) {
                config.setProperty("DB_PASS", line.getOptionValue("dbpassword"));
            }
        } catch( ParseException exp ) {
            System.out.println( "Unexpected exception:" + exp.getMessage() );
        }
    }

}
