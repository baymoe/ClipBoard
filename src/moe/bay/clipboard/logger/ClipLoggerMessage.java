package moe.bay.clipboard.logger;

import moe.bay.clipboard.ClipBoard;
import moe.bay.clipboard.api.ClipChannel;
import moe.bay.clipboard.api.ClipServer;
import moe.bay.clipboard.api.LogType;
import org.javacord.api.entity.auditlog.AuditLogActionType;
import org.javacord.api.entity.auditlog.AuditLogEntry;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;import org.javacord.api.event.message.CachedMessagePinEvent;
import org.javacord.api.event.message.CachedMessageUnpinEvent;
import org.javacord.api.event.message.MessageDeleteEvent;
import org.javacord.api.event.message.MessageEditEvent;
import org.javacord.api.listener.message.CachedMessagePinListener;
import org.javacord.api.listener.message.CachedMessageUnpinListener;
import org.javacord.api.listener.message.MessageDeleteListener;
import org.javacord.api.listener.message.MessageEditListener;

import java.awt.*;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class ClipLoggerMessage implements
        MessageEditListener,
        MessageDeleteListener,
        CachedMessagePinListener,
        CachedMessageUnpinListener {

    private final ClipBoard clip;

    public ClipLoggerMessage(ClipBoard clip){
        this.clip = clip;
    }

    /**
     * This method is called every time a message is deleted.
     *
     * @param event The event.
     */
    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        if (event.getMessageAuthor().isEmpty()) {
            return;
        }
        if (!event.getMessageAuthor().get().isRegularUser()) {
            return;
        }

        event.getServer().ifPresent(dServer -> {
            ClipServer server = new ClipServer(clip, dServer);

            getMessagLogChannels(server).forEach(channel -> {

                // if MessageAuthor isn't available, default to getYourSelf
                User author = clip.getDiscord().getYourself();

                // set author as the MessageAuthor
                if (event.getMessageAuthor().isPresent()) {
                    if (event.getMessageAuthor().get().isRegularUser()) {
                        if (event.getMessageAuthor().get().asUser().isPresent()) {
                            author = event.getMessageAuthor().get().asUser().get();
                        }
                    }
                }

                EmbedBuilder logMessage = getLogMessage(author, ":wastebasket: A message was deleted.", "");
                logMessage.addInlineField("Author", author.getMentionTag());
                event.getServerTextChannel().ifPresent(serverTextChannel -> logMessage.addInlineField("Channel", serverTextChannel.getMentionTag()));
                event.getMessageContent().ifPresent(messageContent -> logMessage.addField("Content", messageContent));
                event.getMessage().ifPresent(message -> {
                    if (!message.getAttachments().isEmpty()) {
                        logMessage.addInlineField("Attachments", String.valueOf(message.getAttachments().size()));
                    }
                });
                try {
                    AuditLogEntry lastMessageDeleteAudit = dServer.getAuditLog(1, AuditLogActionType.MESSAGE_DELETE).get().getEntries().get(0);

                    if (event.getMessageId() == (lastMessageDeleteAudit.getTarget().get().getId())) {
                        logMessage.addField("Executor", lastMessageDeleteAudit.getUser().get().getMentionTag());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    clip.getLogger().exception(clip, e, server);
                }
                logMessage.addField("Timestamp", ClipBoard.getCurrentTimeStamp());
                channel.getChannel().sendMessage(logMessage);
            });
        });
    }

    /**
     * This method is called every time a message is edited.
     *
     * @param event The event.
     */
    @Override
    public void onMessageEdit(MessageEditEvent event) {

        if (event.getMessageAuthor().isEmpty()) {
            return;
        }
        if (!event.getMessageAuthor().get().isRegularUser()) {
            return;
        }

        event.getServer().ifPresent(dServer -> {
            ClipServer server = new ClipServer(clip, dServer);
            getMessagLogChannels(server).forEach(channel -> {

                // if MessageAuthor isn't available, default to getYourSelf
                User author = clip.getDiscord().getYourself();

                // set author as the MessageAuthor
                if (event.getMessageAuthor().isPresent()) {
                    if (event.getMessageAuthor().get().isRegularUser()) {
                        if (event.getMessageAuthor().get().asUser().isPresent()) {
                            author = event.getMessageAuthor().get().asUser().get();
                        }
                    }
                }
                EmbedBuilder logMessage = getLogMessage(author, ":pencil: A message was edited.", "");
                event.getServerTextChannel().ifPresent(serverTextChannel -> logMessage.addInlineField("Channel", serverTextChannel.getMentionTag()));
                event.getOldContent().ifPresent(oldContent -> logMessage.addField("From", oldContent));
                logMessage.addField("To", event.getNewContent());

                event.getMessageAttachments().ifPresent(attachments -> logMessage.addInlineField("Attachments", String.valueOf(attachments.size())));
                logMessage.addField("Timestamp", ClipBoard.getCurrentTimeStamp());

                channel.getChannel().sendMessage(logMessage);

            });

        });

    }

    /**
     * This method is called every time a cached message gets pinned.
     *
     * @param event The event.
     */
    @Override
    public void onCachedMessagePin(CachedMessagePinEvent event) {

        event.getServer().ifPresent(dServer -> {
            ClipServer server = new ClipServer(clip, dServer);
            getMessagLogChannels(server).forEach(channel -> {

               User author = clip.getDiscord().getYourself();
               if (event.getMessageAuthor().asUser().isPresent()) {
                   author = event.getMessageAuthor().asUser().get();
               }

                EmbedBuilder logMessage = getLogMessage(author, ":tack: A message was pinned.", "");
                event.getServerTextChannel().ifPresent(serverTextChannel -> logMessage.addInlineField("Channel", serverTextChannel.getMentionTag()));
                logMessage.addField("Content", event.getMessageContent());
                if (event.getMessageAttachments().size() > 0) {
                    logMessage.addInlineField("Attachments", String.valueOf(event.getMessageAttachments().size()));
                }
                logMessage.addField("Timestamp", ClipBoard.getCurrentTimeStamp());

                channel.getChannel().sendMessage(logMessage);


            });
        });
    }

    /**
     * This method is called every time a cached message gets unpinned.
     *
     * @param event The event.
     */
    @Override
    public void onCachedMessageUnpin(CachedMessageUnpinEvent event) {

        event.getServer().ifPresent(dServer -> {
            ClipServer server = new ClipServer(clip, dServer);
            getMessagLogChannels(server).forEach(channel -> {

                User author = clip.getDiscord().getYourself();
                if (event.getMessageAuthor().asUser().isPresent()) {
                    author = event.getMessageAuthor().asUser().get();
                }

                EmbedBuilder logMessage = getLogMessage(author, ":tack: A message was unpinned.", "");
                event.getServerTextChannel().ifPresent(serverTextChannel -> logMessage.addInlineField("Channel", serverTextChannel.getMentionTag()));
                logMessage.addField("Content", event.getMessageContent());
                if (event.getMessageAttachments().size() > 0) {
                    logMessage.addInlineField("Attachments", String.valueOf(event.getMessageAttachments().size()));
                }
                logMessage.addField("Timestamp", ClipBoard.getCurrentTimeStamp());

                channel.getChannel().sendMessage(logMessage);


            });
        });
    }

    private EmbedBuilder getLogMessage(User author, String description, String url) {
        return new EmbedBuilder()
                .setAuthor(author)
                .setDescription(description)
                .setUrl(url)
                .setColor(new Color(255, 189, 127))
                .setFooter("Powered by ClipBoard", clip.getDiscord().getYourself().getAvatar());
    }

    private Stream<ClipChannel> getMessagLogChannels(ClipServer server) {
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
