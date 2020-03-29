package moe.bay.clipboard.api;

import java.awt.*;

/**
 * @author Bailey Riezebos
 * @version 1.0
 */
public enum LogType {
    EVENT,
    CACHED_MESSAGE_PIN,
    CACHED_MESSAGE_UNPIN,
    CERTAIN_MESSAGE,
    CHANNEL,
    CHANNEL_PINS_UPDATE,
    GROUP_CHANNEL_CHANGE_NAME,
    GROUP_CHANNEL_CREATE,
    GROUP_CHANNEL_DELETE,
    GROUP_CHANNEL_EVENT,
    KNOWN_CUSTOM_EMOJI_CHANGE_NAME,
    KNOWN_CUSTOM_EMOJI_CHANGE_WHITELISTED_ROLES,
    KNOWN_CUSTOM_EMOJI_CREATE,
    KNOWN_CUSTOM_EMOJI_DELETE,
    KNOWN_CUSTOM_EMOJI,





;
//
//
//    CHANNEL(0, "A channel logger logs Channel Create, Delete, Edit (Permissions & Topic) & Move Events"),
//    MESSAGE(1, "A message logger logs Message Edit, Delete, Pin, Unpin and ReactionAdd Events"),
//    ROLE(2, "A role logger logs Role Create, Delete, Move, ColorChange, NameChange, HoistChange and PermissionsChange Events"),
//    SERVER(3, "A server logger logs server events"), //TODO specify all events
//    USER(4, "A user logger logs user events"); //TODO specify all events

//    private final int id;
//    private final String description;
//    private final Color color;

    /**
     * @param description the description of the LogType (what a logger with this type does).
     * @param color
     */
    LogType(
//            String description, Color color
            ) {
//        this.id = id;
//        this.description = description;
//        this.color = color;
    }

    /**
     * @return LogType's id
     */
//    public int getId() {
//        return id;
//    }

    /**
     * @return LogType's description
     */
//    public String getDescription() {
//        return description;
//    }

//    public static LogType getLogTypeFromId(int id) {
//        for (LogType logType : LogType.values()) {
//            if (logType.getId() == id) {
//                return logType;
//            }
//        }
//        return null;
//    }

    public static boolean exists(String name) {
        try {
            LogType.valueOf(name.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
