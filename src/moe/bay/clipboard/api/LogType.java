package moe.bay.clipboard.api;

/**
 * @author Bailey Riezebos
 * @version 1.0
 */
public enum LogType {
    CHANNEL(0, "A channel logger logs Channel Create, Delete, Edit (Permissions & Topic) & Move Events"),
    MESSAGE(1, "A message logger logs Message Edit, Delete, Pin, Unpin and ReactionAdd Events"),
    ROLE(2, "A role logger logs Role Create, Delete, Move, ColorChange, NameChange, HoistChange and PermissionsChange Events"),
    SERVER(3, "A server logger logs server events"), //TODO specify all events
    USER(4, "A user logger logs user events"); //TODO specify all events

    private final int id;
    private final String description;

    /**
     * @param id the ID of the LogType.
     * @param description the description of the LogType (what a logger with this type does).
     */
    LogType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * @return LogType's id
     */
    public int getId() {
        return id;
    }

    /**
     * @return LogType's description
     */
    public String getDescription() {
        return description;
    }

    public static LogType getLogTypeFromId(int id) {
        for (LogType logType : LogType.values()) {
            if (logType.getId() == id) {
                return logType;
            }
        }
        return null;
    }

    public static boolean exists(String name) {
        try {
            LogType.valueOf(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
