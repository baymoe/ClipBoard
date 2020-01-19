package moe.bay.clipboard;

import moe.bay.clipboard.api.ClipServer;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.util.logging.ExceptionLogger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ClipBoard {

    private final Logger logger;
    private final Database database;
    private DiscordApi discord;

    ClipBoard(final Properties properties) throws SQLException {
        this.logger = new Logger(Boolean.valueOf(properties.getProperty("debug")));

        this.database = new Database(
                properties.getProperty("db-host"),
                Integer.parseInt(properties.getProperty("db-port")),
                properties.getProperty("db-db"),
                properties.getProperty("db-user"),
                properties.getProperty("db-pass"),
                Boolean.valueOf(properties.getProperty("debug")));

        new DiscordApiBuilder()
                .setToken(properties.getProperty("discord-token"))
                .setRecommendedTotalShards().join()
                .loginAllShards()
                .forEach(shardFuture -> shardFuture
                        .thenAccept(this::onShardLogin).exceptionally(ExceptionLogger.get())
                );
    }

    public Logger getLogger() {
        return this.logger;
    }

    public Database getDatabase() {
        return this.database;
    }

    public DiscordApi getDiscord() {
        return this.discord;
    }

    public List<ClipServer> getServers() {
        return this.discord.getServers().stream().map((s) -> new ClipServer(this, s)).collect(Collectors.toList());
    }

    private void onShardLogin(final DiscordApi api) {
        this.discord = api;

        System.out.println("Connected to shard " + api.getCurrentShard());
        ArrayList<ClipServer> servers = new ArrayList<>(getServers());

        api.updateActivity(ActivityType.LISTENING, "cb help");

    }
}
