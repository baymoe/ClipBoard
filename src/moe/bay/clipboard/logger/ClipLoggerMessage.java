package moe.bay.clipboard.logger;

import org.javacord.api.event.message.CachedMessagePinEvent;
import org.javacord.api.event.message.CachedMessageUnpinEvent;
import org.javacord.api.event.message.MessageDeleteEvent;
import org.javacord.api.event.message.MessageEditEvent;
import org.javacord.api.listener.message.CachedMessagePinListener;
import org.javacord.api.listener.message.CachedMessageUnpinListener;
import org.javacord.api.listener.message.MessageDeleteListener;
import org.javacord.api.listener.message.MessageEditListener;

public class ClipLoggerMessage implements MessageEditListener, MessageDeleteListener, CachedMessagePinListener, CachedMessageUnpinListener {
    /**
     * This method is called every time a message is deleted.
     *
     * @param event The event.
     */
    @Override
    public void onMessageDelete(MessageDeleteEvent event) {

    }

    /**
     * This method is called every time a message is edited.
     *
     * @param event The event.
     */
    @Override
    public void onMessageEdit(MessageEditEvent event) {

    }

    /**
     * This method is called every time a cached message gets pinned.
     *
     * @param event The event.
     */
    @Override
    public void onCachedMessagePin(CachedMessagePinEvent event) {

    }

    /**
     * This method is called every time a cached message gets unpinned.
     *
     * @param event The event.
     */
    @Override
    public void onCachedMessageUnpin(CachedMessageUnpinEvent event) {

    }
}
