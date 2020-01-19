package moe.bay.clipboard;

import xyz.derkades.derkutils.DatabaseHandler;

import java.sql.SQLException;

/**
 * @author Bailey Riezebos
 * @version 1.0
 * This is the DataBaseHandler that ensures connection to a SQL database for storage and configuration.
 */
public class Database extends DatabaseHandler {

    public Database(final String host, final int port, final String database, final String user, final String password, final boolean debug)
            throws SQLException {
        super(host, port, database, user, password, debug);
    }

}
