package moe.bay.clipboard.logger;

import moe.bay.clipboard.ClipBoard;
import moe.bay.clipboard.api.ClipChannel;
import moe.bay.clipboard.api.ClipServer;
import moe.bay.clipboard.api.LogType;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.server.*;
import org.javacord.api.event.channel.server.text.ServerTextChannelChangeSlowmodeEvent;
import org.javacord.api.event.channel.server.text.ServerTextChannelChangeTopicEvent;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelChangeBitrateEvent;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelChangeUserLimitEvent;
import org.javacord.api.listener.channel.group.GroupChannelCreateListener;
import org.javacord.api.listener.channel.server.*;
import org.javacord.api.listener.channel.server.text.ServerTextChannelChangeSlowmodeListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelChangeTopicListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelChangeBitrateListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelChangeUserLimitListener;

import java.awt.*;
import java.sql.SQLException;
import java.util.stream.Stream;

public class ClipLoggerChannel implements
        ServerChannelCreateListener,
        ServerChannelDeleteListener,
        ServerChannelChangeNameListener,
        ServerChannelChangePositionListener,
        ServerChannelChangeOverwrittenPermissionsListener,
        ServerChannelChangeNsfwFlagListener,
        ServerTextChannelChangeSlowmodeListener,
        ServerTextChannelChangeTopicListener,
        ServerVoiceChannelChangeBitrateListener,
        ServerVoiceChannelChangeUserLimitListener {


    private final ClipBoard clip;

    public ClipLoggerChannel(ClipBoard clip) {
        this.clip = clip;
    }

    /**
     * This method is called every time a server channel's name changes.
     *
     * @param event The event.
     */
    @Override
    public void onServerChannelChangeName(ServerChannelChangeNameEvent event) {

    }

    /**
     * This method is called every time a server channel's nsfw flag changes.
     *
     * @param event The event.
     */
    @Override
    public void onServerChannelChangeNsfwFlag(ServerChannelChangeNsfwFlagEvent event) {

    }

    /**
     * This method is called every time a server channel's overwritten permissions change.
     *
     * @param event The event.
     */
    @Override
    public void onServerChannelChangeOverwrittenPermissions(ServerChannelChangeOverwrittenPermissionsEvent event) {

    }

    /**
     * This method is called every time a server channel's position changes.
     *
     * @param event The event.
     */
    @Override
    public void onServerChannelChangePosition(ServerChannelChangePositionEvent event) {

    }

    /**
     * This method is called every time a server channel is created.
     *
     * @param event The event.
     */
    @Override
    public void onServerChannelCreate(ServerChannelCreateEvent event) {

    }

    /**
     * This method is called every time a server channel is deleted.
     *
     * @param event The event.
     */
    @Override
    public void onServerChannelDelete(ServerChannelDeleteEvent event) {

    }

    /**
     * This method is called every time a server text channel's slowmode delay changes.
     *
     * @param event The event.
     */
    @Override
    public void onServerTextChannelChangeSlowmodeDelay(ServerTextChannelChangeSlowmodeEvent event) {

    }

    /**
     * This method is called every time a server text channel's topic changes.
     *
     * @param event The event.
     */
    @Override
    public void onServerTextChannelChangeTopic(ServerTextChannelChangeTopicEvent event) {

    }

    /**
     * This method is called every time a server voice channel's bitrate changes.
     *
     * @param event The event.
     */
    @Override
    public void onServerVoiceChannelChangeBitrate(ServerVoiceChannelChangeBitrateEvent event) {

    }

    /**
     * This method is called every time a server voice channel's user limit changes.
     *
     * @param event The event.
     */
    @Override
    public void onServerVoiceChannelChangeUserLimit(ServerVoiceChannelChangeUserLimitEvent event) {

    }

    private EmbedBuilder getLogMessage(User author, String description, String url) {
        return new EmbedBuilder()
                .setAuthor(author)
                .setDescription(description)
                .setUrl(url)
                .setColor(new Color(255, 107, 129))
                .setFooter("Powered by ClipBoard", clip.getDiscord().getYourself().getAvatar());
    }

    private Stream<ClipChannel> getChannelLogChannels(ClipServer server) {
        return server.getClipChannels().stream().filter(channel -> {
            try {
                return channel.getLoggers().contains(LogType.MESSAGE);
            } catch (SQLException e) {
                clip.getLogger().exception(clip, e, server);
                return false;
            }
        });
    }
}