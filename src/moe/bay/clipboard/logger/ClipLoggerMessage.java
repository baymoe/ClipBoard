package moe.bay.clipboard.logger;

import moe.bay.clipboard.ClipBoard;
import moe.bay.clipboard.api.ClipServer;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.CachedMessagePinEvent;
import org.javacord.api.event.message.CachedMessageUnpinEvent;
import org.javacord.api.event.message.MessageDeleteEvent;
import org.javacord.api.event.message.MessageEditEvent;
import org.javacord.api.listener.message.CachedMessagePinListener;
import org.javacord.api.listener.message.CachedMessageUnpinListener;
import org.javacord.api.listener.message.MessageDeleteListener;
import org.javacord.api.listener.message.MessageEditListener;

import java.awt.datatransfer.Clipboard;
import java.util.Optional;

public class ClipLoggerMessage implements MessageEditListener, MessageDeleteListener, CachedMessagePinListener, CachedMessageUnpinListener {

    /**
     * This method is called every time a message is deleted.
     *
     * @param event The event.
     */
    @Override
    public void onMessageDelete(MessageDeleteEvent event) {

        Optional<MessageAuthor> author = event.getMessageAuthor();

        event.getServer().ifPresent(server -> {
            new ClipServer(server);
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
}
