package Model;

import java.util.ArrayList;

public class Chat {
    private final ArrayList<User> members = new ArrayList<>();
    private final ArrayList<Message> messages = new ArrayList<>();
    private final User owner;
    private final String id;
    private final String name;

    public void addMember(User user) {
        members.add(user);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public User getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Chat(User owner, String id, String name) {
        this.owner = owner;
        this.id = id;
        this.name = name;
        addMember(owner);
    }
}
