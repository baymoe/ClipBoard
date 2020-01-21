package moe.bay.clipboard;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;

/**
 * @author Bailey Riezebos
 * @version 1.0
 * This is the main class that will run the startup configuration and load properties.
 */
public class App {

    /**
     * @param args
     * if argument provided, <code>args[0]</code> is used as the filename for properties. <br>
     * if left blank, environment variables will be used in stead.
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        final Properties properties = new Properties();

        if (args.length > 0) {
            final File propFile = new File(args[0]);
            if (!propFile.exists()) {
                final String contents =
                        "discord-token=\n"
                                + "db-host=\n"
                                + "db-port=\n"
                                + "db-name=\n"
                                + "db-user=\n"
                                + "db-pass=\n"
                                + "debug=true";
                FileUtils.write(propFile, contents, "UTF-8");
                System.out.println("Please set up the configuration file.");
                return;
            }
            try (InputStream stream = new FileInputStream(propFile)) {
                properties.load(stream);
            }
        } else {
            properties.setProperty("discord-token", System.getenv("CLIPBOARD_TOKEN"));
            properties.setProperty("db-host", System.getenv("CLIPBOARD_DB_HOST"));
            properties.setProperty("db-port", System.getenv("CLIPBOARD_DB_PORT"));
            properties.setProperty("db-name", System.getenv("CLIPBOARD_DB_NAME"));
            properties.setProperty("db-user", System.getenv("CLIPBOARD_DB_USER"));
            properties.setProperty("db-pass", System.getenv("CLIPBOARD_DB_PASS"));
            properties.setProperty("debug", System.getenv("CLIPBOARD_DEBUG"));
        }

        try {
            new ClipBoard(properties);
        } catch (SQLException e) {
            System.out.println("Could not connect to database, stopping ClipBoard.");
            e.printStackTrace();
        }
    }
}
