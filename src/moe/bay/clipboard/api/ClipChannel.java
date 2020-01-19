package moe.bay.clipboard.api;

import moe.bay.clipboard.ClipBoard;
import org.javacord.api.entity.channel.ServerTextChannel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bailey Riezebos
 * @version 1.0
 */
public class ClipChannel {

    private final ServerTextChannel channel;
    private final ClipServer server;

    public ClipChannel(ClipServer server, ServerTextChannel channel) {
        this.channel = channel;
        this.server = server;
    }

    public ServerTextChannel getChannel() {
        return channel;
    }

    public long getId() {
        return channel.getId();
    }

    public List<LogType> getLoggers() throws SQLException {
        try (final PreparedStatement s = server.getClip().getDatabase().prepareStatement(
                "SELECT `log_type` FROM `server_log_channels` WHERE `server_id`=? AND `channel_id`=?", server.getId(), getId())) {
            ResultSet r = s.executeQuery();
            List<LogType> logTypes = new ArrayList<>();
            while (r.next()) {
                logTypes.add(LogType.getLogTypeFromId(r.getInt(0)));
            }
            return logTypes;
        }
    }

    public void addLogger(final LogType logType) throws SQLException {
        try (final PreparedStatement s = server.getClip().getDatabase().prepareStatement(
                "INSERT INTO `server_log_channels` (`server_id`, `channel_id`, `log_type`) VALUES (?, ?, ?)",
                server.getId(), getId(), logType)) {
            s.execute();
        }
    }

    public void removeLogger(final LogType logType) throws SQLException {
        try (final PreparedStatement s = server.getClip().getDatabase().prepareStatement(
                "DELETE FROM `server_log_channels` WHERE `server_id`=? `channel_id`=? `log_type`=?",
                server.getId(), getId(), logType)) {
            s.execute();
        }
    }
}