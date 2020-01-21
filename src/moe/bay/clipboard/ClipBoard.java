package moe.bay.clipboard;

import moe.bay.clipboard.api.ClipChannel;
import moe.bay.clipboard.api.ClipServer;
import moe.bay.clipboard.commands.LogCommand;
import moe.bay.clipboard.logger.ClipLoggerMessage;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.util.logging.ExceptionLogger;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Bailey Riezebos
 * @version 1.0
 * This is a ClipBoard object that acts as the instance of the Discord Bot.
 */
public class ClipBoard {

    private final Logger logger;
    private final Database database;
    private DiscordApi discord;

    ClipBoard(final Properties properties) throws SQLException {
        this.logger = new Logger(Boolean.parseBoolean(properties.getProperty("debug")));

        this.database = new Database(
                properties.getProperty("db-host"),
                Integer.parseInt(properties.getProperty("db-port")),
                properties.getProperty("db-name"),
                properties.getProperty("db-user"),
                properties.getProperty("db-pass"),
                Boolean.parseBoolean(properties.getProperty("debug")));

        new DiscordApiBuilder()
                .setToken(properties.getProperty("discord-token"))
                .setRecommendedTotalShards().join()
                .loginAllShards()
                .forEach(shardFuture -> shardFuture
                        .thenAccept(this::onShardLogin).exceptionally(ExceptionLogger.get())
                );
    }

    /**
     * @return Application-level logger (not ClipLoggers)
     */
    public Logger getLogger() { return this.logger; }

    /**
     * @return Database used to store log channels and configurations
     */
    public Database getDatabase() { return this.database; }

    /**
     * @return ClipBoard's DiscordApi
     */
    public DiscordApi getDiscord() { return this.discord; }


    /**
     * @return a list of all ClipServers that ClipBoard is in
     */
    public List<ClipServer> getServers() {
        return this.discord.getServers().stream().map((s) -> new ClipServer(this, s)).collect(Collectors.toList());
    }

    /**
     * @return The ClipServer used as a "Home" or support server for the bot
     */
    public ClipServer getHome() {
        return new ClipServer(this, this.getDiscord().getServerById(668450061625327633L).get());
    }

    /**
     * @return The ClipChannel used to log errors that occur while using the bot
     */
    public ClipChannel getErrorLogChannel() {
        return new ClipChannel(getHome(), getHome().getServer().getTextChannelById(668528956726706217L).get());
    }

    private void onShardLogin(final DiscordApi api) {
        this.discord = api;

        System.out.println("Connected to shard " + api.getCurrentShard());
        ArrayList<ClipServer> servers = new ArrayList<>(getServers());

        api.updateActivity(ActivityType.LISTENING, "cb help");

        api.addListener(new ClipLoggerMessage(this));
        api.addListener(new LogCommand(this));
    }

    /**
     * @return Formatted timestamp of when the method was called
     */
    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss UTC").format(Instant.now());
    }
}
