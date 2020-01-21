package moe.bay.clipboard.commands;

import moe.bay.clipboard.ClipBoard;
import moe.bay.clipboard.api.ClipChannel;
import moe.bay.clipboard.api.ClipServer;
import moe.bay.clipboard.api.LogType;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class LogCommand implements MessageCreateListener {

    private final ClipBoard clip;

    public LogCommand(ClipBoard clip) {
        this.clip = clip;
    }

    /**
     * This method is called every time a message is created.
     *
     * @param event The event.
     */
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        List<String> args = Arrays.asList(event.getMessageContent().split(" "));
        if (event.getServer().isEmpty() || event.getServerTextChannel().isEmpty()) {
            event.getChannel().sendMessage("Sorry, ClipBoard only works with in servers, DM Loggers will not work.");
            return;
        }
        ClipServer server = new ClipServer(clip, event.getServer().get());
        ClipChannel channel = new ClipChannel(server, event.getServerTextChannel().get());
        if (event.getMessageAuthor().asUser().isEmpty()) {
            return;
        }

        if (args.size() > 0 && args.get(0).equals("cb")) {
            if (!server.getServer().hasPermission(event.getMessageAuthor().asUser().get(), PermissionType.MANAGE_SERVER)) {
                event.getChannel().sendMessage("Sorry, ClipBoard can only be managed by server administrators.");
            }
            if (args.size() > 1 && args.get(1).equals("log") && event.getChannel().asTextChannel().isPresent()) {
                if (args.size() > 2 && args.get(2).equals("add")) {
                    if (args.size() > 3 && LogType.exists(args.get(3).toUpperCase())) {
                        if (args.size() > 4) {
                            try {
                                server.addLogger(LogType.valueOf(args.get(3).toUpperCase()),
                                        new ClipChannel(new ClipServer(clip, event.getServer().get())
                                                , event.getMessage().getMentionedChannels().get(0)));
                            } catch (SQLException e) {
                                clip.getLogger().exception(clip, e);
                            }
                        } else {
                            try {
                                channel.addLogger(LogType.valueOf(args.get(3).toUpperCase()));
                            } catch (SQLException e) {
                                clip.getLogger().exception(clip, e);
                            }
                        }
                        event.getChannel().sendMessage("ok");
                    } else {
                        event.getChannel().sendMessage(args.get(3) + "is not a valid Log Type.");
                    }
                }
                else if (args.size() > 2) {
                    if (args.get(2).equals("remove")) {
                        if (args.size() > 3 && LogType.exists(args.get(3).toUpperCase())) {
                            if (args.size() > 4) {
                                try {
                                    server.removeLogger(LogType.valueOf(args.get(3).toUpperCase()),
                                            new ClipChannel(new ClipServer(clip, event.getServer().get())
                                                    , event.getMessage().getMentionedChannels().get(0)));
                                } catch (SQLException e) {
                                    clip.getLogger().exception(clip, e);
                                }
                            } else {
                                try {
                                    channel.removeLogger(LogType.valueOf(args.get(3).toUpperCase()));
                                } catch (SQLException e) {
                                    clip.getLogger().exception(clip, e);
                                }
                            }
                            event.getChannel().sendMessage("ok");
                        } else {
                            event.getChannel().sendMessage(args.get(3) + "is not a valid Log Type.");
                        }
                    }
                    else {
                        event.getChannel().sendMessage("Invalid subcommand (" + args.get(2) + ") for " + args.get(1) + ".");
                    }
                }
            } else {
                event.getChannel().sendMessage("Invalid command (" + args.get(1) + ").");
            }
        }
    }
}