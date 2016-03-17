package Interface;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration2.Configuration;

import static Interface.CommandParser.parse;

/**
 * Created by anthonymonori on 13/03/16.
 */
public class MainApplication {

    public static void main(String[] args) {
        Configuration config = ConfigurationManager.getInstance().getConfiguration();

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

        parse(config, options, args);


    }

}
