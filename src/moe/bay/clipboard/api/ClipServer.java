package moe.bay.clipboard.api;

import moe.bay.clipboard.ClipBoard;
import org.javacord.api.entity.server.Server;
import xyz.derkades.derkutils.caching.Cache;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bailey Riezebos
 * @version 1.0
 */
public class ClipServer {

    private final String DEFAULT_PREFIX = "cb ";

    private final ClipBoard clip;
    private final Server server;

    public ClipServer(final ClipBoard clip, final Server server) {
        this.clip = clip;
        this.server = server;
    }

    public ClipBoard getClip() {
        return clip;
    }

    public Server getServer() {
        return server;
    }

    public String getName() {
        return server.getName();
    }

    public long getId() {
        return server.getId();
    }

    public String getPrefix() throws SQLException {
        final Object cache = Cache.getCachedObject("server_prefix" + server.getId());

        if (cache != null) {
            return (String) cache;
        }

        try (final PreparedStatement s = clip.getDatabase().prepareStatement("SELECT `prefix` FROM `server_command_prefixes` WHERE `server_id`=?", server.getId())) {
            final ResultSet result = s.executeQuery();
            String prefix;
            if (!result.next()) {
                prefix = DEFAULT_PREFIX;
            } else {

                prefix = result.getString(1);
            }

            Cache.addCachedObject("server_prefix" + server.getId(), prefix);
            return prefix;
        }
    }

    public void setPrefix(final String prefix) throws SQLException {
        Cache.addCachedObject("server_prefix" + server.getId(), prefix);

        try (final PreparedStatement s = clip.getDatabase().prepareStatement(
                "INSERT INTO `server_command_prefixes` (`server_id`, `prefix`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `prefix`=?",
                server.getId(), prefix, prefix)) {
            s.execute();
        }
    }

    public List<ClipChannel> getClipChannels() {
        return server.getTextChannels().stream().map((s) -> new ClipChannel(this, s)).collect(Collectors.toList());
    }

    public void addLogger(final LogType logType, ClipChannel channel) throws SQLException {
        try (final PreparedStatement s = getClip().getDatabase().prepareStatement(
                "INSERT INTO `log_channels` (`server_id`, `channel_id`, `log_type`) VALUES (?, ?, ?)",
                server.getId(), channel.getId(), logType.name())) {
            s.execute();
        }
    }

    public void removeLogger(final LogType logType, ClipChannel channel) throws SQLException {
        try (final PreparedStatement s = getClip().getDatabase().prepareStatement(
                "DELETE FROM `log_channels` WHERE `server_id`=? AND `channel_id`=? AND `log_type`=?",
                server.getId(), getId(), logType.name())) {
            s.execute();
        }
    }

//    public List<ClipChannel> getLogChannels() {
//        getClipChannels()
//    }
}
