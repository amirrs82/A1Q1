package Model;

import java.util.ArrayList;

public class User {
    private final ArrayList<Chat> chats = new ArrayList<>();
    private final String id;
    private final String name;
    private final String password;

    public void addChat(Chat chat) {
        chats.add(chat);
    }

    public void addGroup(Group group) {
        chats.add(group);
    }

    public void addChannel(Channel channel) {
        chats.add(channel);
    }

    public void addPrivateChat(PrivateChat pv) {
        chats.add(pv);
    }

    public Group getGroupById(String id) {
        for (Chat chat : chats)
            if (chat.getClass().equals(Group.class))
                if (chat.getId().equals(id))
                    return (Group) chat;
        return null;
    }

    public Channel getChannelById(String id) {
        for (Chat chat : chats)
            if (chat.getClass().equals(Channel.class))
                if (chat.getId().equals(id))
                    return (Channel) chat;
        return null;
    }

    public PrivateChat getPrivateChatById(String id) {
        for (Chat chat : chats)
            if (chat.getClass().equals(PrivateChat.class))
                if (chat.getId().equals(id))
                    return (PrivateChat) chat;
        return null;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
