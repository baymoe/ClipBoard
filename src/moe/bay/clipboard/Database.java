package moe.bay.clipboard;

import xyz.derkades.derkutils.DatabaseHandler;

import java.sql.SQLException;

public class Database extends DatabaseHandler {

    public Database(final String host, final int port, final String database, final String user, final String password, final boolean debug)
            throws SQLException {
        super(host, port, database, user, password, debug);
    }

}
