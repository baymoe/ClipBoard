package moe.bay.clipboard.api;

import moe.bay.clipboard.ClipBoard;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import xyz.derkades.derkutils.Colors;

import java.awt.*;
import java.sql.SQLException;
import java.util.stream.Stream;

public interface ClipLogger {

    private EmbedBuilder getLogMessage(ClipBoard clip, User author, LogType logType) {
        return new EmbedBuilder()
                .setAuthor(author)
                .setDescription(logType.getDescription())
                .setColor(Colors.getColorFromHex(logType.getColor()))
                .setFooter("Powered by ClipBoard", clip.getDiscord().getYourself().getAvatar());
    }

    private Stream<ClipChannel> getMessagLogChannels(ClipBoard clip, ClipServer server, LogType logType) {
        return server.getClipChannels().stream().filter(channel -> {
            try {
                return channel.getLoggers().contains(logType);
            } catch (SQLException e) {
                clip.getLogger().exception(clip, e, server);
                return false;
            }
        });
    }
}
