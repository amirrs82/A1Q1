package Model;

public class PrivateChat extends Chat {

    public PrivateChat(User owner, String id, String name) {
        super(owner, id, name);
    }

    @Override
    public String getId() {
        if (getMembers().get(0).equals(Messenger.getCurrentUser()))
            return getMembers().get(1).getId();
        return getMembers().get(0).getId();
    }

    @Override
    public String getName() {
        if (getMembers().get(0).equals(Messenger.getCurrentUser()))
            return getMembers().get(1).getName();
        return getMembers().get(0).getName();
    }
}
