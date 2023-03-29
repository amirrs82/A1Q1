package View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    REGISTER("(?<command>register) i (?<id>\\S+) u (?<username>\\S+) p (?<password>\\S+)"),
    LOGIN("(?<command>login) i (?<id>\\S+) p (?<password>\\S+)"),
    SHOW_ALL_CHANNELS("(?<command>show all channels)"),
    CREATE_CHANNEL("(?<command>create new channel) i (?<id>\\S+) n (?<name>\\S+)"),
    JOIN_CHANNEL("(?<command>join channel) i (?<id>\\S+)"),
    SHOW_MY_CHATS("(?<command>show my chats)"),
    CREATE_NEW_GROUP("(?<command>create new group) i (?<id>\\S+) n (?<name>\\S+)"),
    START_NEW_PRIVATE_CHAT("(?<command>start a new private chat with) i (?<id>\\S+)"),
    ENTER_CHAT("(?<command>enter) (?<chatType>group|channel|private chat) i (?<id>\\S+)"),
    SEND_MESSAGE("(?<command>send a message) c (?<message>.+)"),
    ADD_MEMBER("(?<command>add member) i (?<id>\\S+)"),
    SHOW_ALL_MESSAGES("(?<command>show all messages)"),
    SHOW_ALL_MEMBERS("(?<command>show all members)");
    private final String regex;

    Commands(String regex) {
        this.regex = regex;
    }

    public static Matcher getMatcher(String input, Commands command) {
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        if (matcher.matches()) return matcher;
        return null;
    }
}