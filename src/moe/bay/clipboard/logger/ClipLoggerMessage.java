package moe.bay.clipboard.logger;

import moe.bay.clipboard.ClipBoard;
import moe.bay.clipboard.api.ClipServer;
import moe.bay.clipboard.api.LogType;
import org.javacord.api.entity.auditlog.AuditLogActionType;
import org.javacord.api.entity.auditlog.AuditLogEntry;
import org.javacord.api.entity.message.MessageAuthor;
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
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ClipLoggerMessage implements MessageEditListener, MessageDeleteListener, CachedMessagePinListener, CachedMessageUnpinListener {

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
        event.getServer().ifPresent(server -> {
            ClipServer clipServer = new ClipServer(clip, server);
            clipServer.getClipChannels().stream().filter(channel -> {
                try {
                    return channel.getLoggers().contains(LogType.MESSAGE);
                } catch (SQLException e) {
                    clip.getLogger().exception(clip, e, clipServer);
                    return false;
                }
            }).forEach(clipChannel -> {
                User author;
                if (event.getMessageAuthor().isPresent()
                        && event.getMessageAuthor().get().isRegularUser()
                        && event.getMessageAuthor().get().asUser().isPresent()) {
                        author = event.getMessageAuthor().get().asUser().get();
                    } else {
                    author = clip.getDiscord().getYourself();
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
                    AuditLogEntry lastMessageDeleteAudit = server.getAuditLog(1, AuditLogActionType.MESSAGE_DELETE).get().getEntries().get(0);

                    if (event.getMessageId() == (lastMessageDeleteAudit.getTarget().get().getId())) {
                        logMessage.addField("Executor", lastMessageDeleteAudit.getUser().get().getMentionTag());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    clip.getLogger().exception(clip, e, clipServer);
                }
                clipChannel.getChannel().sendMessage(logMessage);
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
        Optional<MessageAuthor> author = event.getMessageAuthor();
    }

    /**
     * This method is called every time a cached message gets pinned.
     *
     * @param event The event.
     */
    @Override
    public void onCachedMessagePin(CachedMessagePinEvent event) {
        MessageAuthor author = event.getMessageAuthor();
    }

    /**
     * This method is called every time a cached message gets unpinned.
     *
     * @param event The event.
     */
    @Override
    public void onCachedMessageUnpin(CachedMessageUnpinEvent event) {
        MessageAuthor author = event.getMessageAuthor();
    }

    private EmbedBuilder getLogMessage(User author, String description, String url) {
        return new EmbedBuilder()
                .setAuthor(author)
                .setDescription(description)
                .setUrl(url)
                .setColor(new Color(255, 189, 127))
                .setFooter("Powered by ClipBoard", clip.getDiscord().getYourself().getAvatar());
    }
}
