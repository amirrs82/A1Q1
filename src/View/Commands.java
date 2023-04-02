package View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    REGISTER("register i (?<id>\\S+) u (?<username>\\S+) p (?<password>\\S+)"),
    LOGIN("login i (?<id>\\S+) p (?<password>\\S+)"),
    SHOW_ALL_CHANNELS("show all channels"),
    CREATE_CHANNEL("create new channel i (?<id>\\S+) n (?<name>\\S+)"),
    JOIN_CHANNEL("join channel i (?<id>\\S+)"),
    SHOW_MY_CHATS("show my chats"),
    CREATE_NEW_GROUP("create new group i (?<id>\\S+) n (?<name>\\S+)"),
    START_NEW_PRIVATE_CHAT("start a new private chat with i (?<id>\\S+)"),
    ENTER_CHAT("enter (?<chatType>group|channel|private chat) i (?<id>\\S+)"),
    SEND_MESSAGE("send a message c (?<message>.+)"),
    ADD_MEMBER("add member i (?<id>\\S+)"),
    SHOW_ALL_MESSAGES("show all messages"),
    SHOW_ALL_MEMBERS("show all members");
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