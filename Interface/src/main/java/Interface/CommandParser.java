package Interface;

import org.apache.commons.cli.*;
import org.apache.commons.configuration2.Configuration;

import static BusinessLogicLayer.EnronSearchEngine.run;

/**
 * Created by anthonymonori on 17/03/16.
 */
public class CommandParser {

    private CommandParser(){};

    public static void parse(Configuration mConfig, Options options, String[] args) {

            CommandLineParser parser = new DefaultParser();

            try {
            CommandLine line = parser.parse(options, args);
            // these can only be called by themselves
            if (line.hasOption("help")) {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("search-indexer", options);

                return;
            }
            if (line.hasOption("showsettings")) {
                System.out.println("DB_TYPE=" + (mConfig.containsKey("DB_TYPE") ? mConfig.getString("DB_TYPE") : ""));
                System.out.println("DB_URL=" + (mConfig.containsKey("DB_URL") ? mConfig.getString("DB_URL") : ""));
                System.out.println("DB_USER=" + (mConfig.containsKey("DB_USER") ? mConfig.getString("DB_USER") : ""));
                System.out.println("DB_PASSWORD=" + (mConfig.containsKey("DB_PASS") ? mConfig.getString("DB_PASS") : ""));

                System.out.println("ID_DEFAULT_FOLDER_TO_MONITOR=" + (mConfig.containsKey("ID_DEFAULT_FOLDER_TO_MONITOR") ? mConfig.getString("ID_DEFAULT_FOLDER_TO_MONITOR") : ""));
                System.out.println("ID_EXTENSION=" + (mConfig.containsKey("ID_EXTENSION") ? mConfig.getString("ID_EXTENSION") : ""));
                return;
            }
            if (line.hasOption("run")) {
                String path = line.getOptionValue("run");
                mConfig.setProperty("ID_DEFAULT_FOLDER_TO_MONITOR", path);
                System.out.println("Starting indexing: " + path);
                try {
                    run(mConfig);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

            // these can be called at the same time
            if (line.hasOption("dbtype")) {
                System.out.println("Setting the database type...");
                switch (line.getOptionValue("dbtype")) {
                    case "mysql":
                        mConfig.setProperty("DB_TYPE", "mysql");
                        break;
                    default:
                        System.out.println("The specified database type " + line.getOptionValue("dbtype") + " is unknown.");
                }
            }
            if (line.hasOption("dbhost")) {
                if (mConfig != null) {
                    System.out.println("Setting the database hostname...");
                    mConfig.setProperty("DB_HOSTNAME", line.getOptionValue("dbhost"));
                }
            }
            if (line.hasOption("dbname")) {
                if (mConfig != null) {
                    System.out.println("Setting the database name...");
                    mConfig.setProperty("DB_NAME", line.getOptionValue("dbname"));
                }
            }
            if (line.hasOption("dbuser")) {
                if (mConfig != null) {
                    System.out.println("Setting the database username...");
                    mConfig.setProperty("DB_USER", line.getOptionValue("dbuser"));
                }
            }
            if (line.hasOption("dbpass")) {
                if (mConfig != null) {
                    System.out.println("Setting the database password...");
                    mConfig.setProperty("DB_PASS", line.getOptionValue("dbpass"));
                }
            }
        } catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }

    }
}
