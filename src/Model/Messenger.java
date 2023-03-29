package Model;

import java.util.ArrayList;

public class Messenger {
    private static final ArrayList<Channel> channels = new ArrayList<>();
    private static final ArrayList<Group> groups = new ArrayList<>();
    private static final ArrayList<User> users = new ArrayList<>();
    private static User currentUser;

    public static void setCurrentUser(User currentUser) {
        Messenger.currentUser = currentUser;
    }

    public static void addGroup(Group group) {
        groups.add(group);
    }

    public static void addChannel(Channel channel) {
        channels.add(channel);
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static Group getGroupById(String id) {
        for (Group group : groups)
            if (group.getId().equals(id)) return group;
        return null;
    }

    public static Channel getChannelById(String id) {
        for (Channel channel : channels)
            if (channel.getId().equals(id)) return channel;
        return null;
    }

    public static User getUserById(String id) {
        for (User user : users)
            if (user.getId().equals(id)) return user;
        return null;
    }

    public static ArrayList<Channel> getChannels() {
        return channels;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
