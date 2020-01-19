package moe.bay.clipboard.api;

import moe.bay.clipboard.ClipBoard;
import org.javacord.api.entity.channel.ServerTextChannel;

import java.util.List;

public class ClipLogChannel {

    private final ClipBoard clip;
    private final ServerTextChannel channel;
    private final ClipServer server;

    public ClipLogChannel(ClipBoard clip, ServerTextChannel channel, ClipServer server) {
        this.clip = clip;
        this.channel = channel;
        this.server = server;
    }

    public ClipBoard getClip() {
        return clip;
    }

    public ServerTextChannel getChannel() {
        return channel;
    }

    public long getId() {
        return channel.getId();
    }

    public void addLogger(LogType logType) {

    }

    public List<ClipLogger> getLoggers() {
        return null;
    }
}