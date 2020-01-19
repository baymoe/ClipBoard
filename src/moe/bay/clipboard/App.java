package moe.bay.clipboard;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Bailey Riezebos
 * @version 1.0
 * This is the main class that will run the startup configuration and load properties.
 */
public class App {
    public static void main(String[] args) throws IOException, SQLException {

        final File propFile = new File("clipboard.conf");

        if (!propFile.exists()) {
            final String contents =
                    "discord-token-\n"
                            + "db-host=\n"
                            + "db-port=\n"
                            + "db-user=\n"
                            + "db-pass=\n"
                            + "debug=true";
            FileUtils.write(propFile, contents, "UTF-8");
            System.out.println("Please set up the configuration file.");
            return;
        }

        final Properties properties = new Properties();

        try (InputStream stream = new FileInputStream(propFile)) {
            properties.load(stream);
        }

        try {
            new ClipBoard(properties);
        } catch (SQLException e) {
            System.out.println("Could not connect to database, stopping ClipBoard.");
            e.printStackTrace();
            return;
        }
    }
}
