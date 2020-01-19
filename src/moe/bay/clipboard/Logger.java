package moe.bay.clipboard;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private final File infoFile;
    private final File errorFile;
    private final boolean debug;

    Logger(final boolean debug){
        this.debug = debug;
        this.infoFile = new File("clipboard.info.log");
        this.errorFile = new File("clipboard.error.log");
    }

    public void severe(final String message, final Object... formatObjects) {
        final String text = format("SEVERE", message, formatObjects);
        System.out.println(text);
        append(this.errorFile, text);
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
