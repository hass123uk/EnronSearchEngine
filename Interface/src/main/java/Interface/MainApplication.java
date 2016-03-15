package dk.easv.SearchIndexer.Interface;

import org.apache.commons.cli.*;

/**
 * Created by anthonymonori on 13/03/16.
 */
public class MainApplication {

    public static void main(String[] args) {
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
            if(line.hasOption("help") || line.getArgList().isEmpty()) {
                // automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "search-indexer", options );

                return;
            }
            if(line.hasOption("dbtype")) {

                return;
            }
        } catch( ParseException exp ) {
            System.out.println( "Unexpected exception:" + exp.getMessage() );
        }
    }

}
