package moe.bay.clipboard;

import moe.bay.clipboard.api.ClipChannel;
import moe.bay.clipboard.api.ClipServer;
import org.apache.commons.io.FileUtils;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Bailey Riezebos
 * @version 1.0
 * This is a private logger that keeps track of the bot status.
 */
public class Logger {

    private final File infoFile;
    private final File errorFile;
    private final boolean debug;

    Logger(final boolean debug){
        this.debug = debug;
        this.infoFile = new File("clipboard.info.log");
        this.errorFile = new File("clipboard.error.log");
    }

    public void exception(final ClipBoard clip, final Exception e) {

        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement s : e.getStackTrace()) {
            if (s.getClassName().contains("clipboard")) {
                stackTrace.append("in class ").append(s.getClassName()).append("\n");
                stackTrace.append("in method ").append(s.getMethodName()).append("\n");
                stackTrace.append("on line ").append(s.getLineNumber()).append("\n");
            }
        }

        clip.getErrorLogChannel().getChannel().sendMessage(new EmbedBuilder()
                .setAuthor(clip.getDiscord().getYourself())
                .setDescription("`" + e.getMessage() + "`")
                .addField("StackTrace",
                        "```" + (stackTrace.length() > 750
                                ? stackTrace.substring(0, 750)
                                : stackTrace.toString())
                                + "...``` Read the console for the full StackTrace."));

        e.printStackTrace();
    }

    public void exception(final ClipBoard clip, final Exception e, final ClipServer server) {
        clip.getErrorLogChannel().getChannel().sendMessage(new EmbedBuilder()
                .setAuthor(server.getName(), "",
                        server.getServer().getIcon().isPresent()
                                ? server.getServer().getIcon().get().getUrl().toString()
                                : clip.getHome().getServer().getIcon().get().getUrl().toString())
                .setDescription(e.getMessage())
                .addField("StackTrace", "```" + Arrays.toString(e.getStackTrace()) + "```"));
    }

    public void warning(final String message, final Object... formatObjects) {
        final String text = format("WARNING", message, formatObjects);
        System.out.println(text);
        append(this.errorFile, text);
    }

    public void info(final String message, final Object... formatObjects) {
        final String text = format("INFO", message, formatObjects);
        System.out.println(text);
        append(this.infoFile, text);
    }

    public void debug(final String message, final Object... formatObjects) {
        if (this.debug) {
            System.out.println(format("DEBUG", message, formatObjects));
        }
    }

    private String format(final String level, final String text, final Object... formatObjects) {
        final String time = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
        return String.format("[%s] [%s] %s", time, level, String.format(text, formatObjects));
    }

    private void append(final File file, final String text) {
        try {
            FileUtils.writeStringToFile(file, text, "UTF-8", true);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
