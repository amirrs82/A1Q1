package View;

import Model.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessengerMenu {
    private Chat chat;
    private User currentUser;

    public void run(Scanner scanner) {
        currentUser = Messenger.getCurrentUser();
        while (true) {
            String input = scanner.nextLine();
            Matcher matcher;
            if (input.equals("logout")) {
                System.out.println("Logged out");
                break;
            } else if (Commands.getMatcher(input, Commands.SHOW_ALL_CHANNELS) != null)
                System.out.println(showAllChannels());
            else if ((matcher = Commands.getMatcher(input, Commands.CREATE_CHANNEL)) != null)
                System.out.println(createChannel(matcher));
            else if ((matcher = Commands.getMatcher(input, Commands.JOIN_CHANNEL)) != null)
                System.out.println(joinChannel(matcher));
            else if (Commands.getMatcher(input, Commands.SHOW_MY_CHATS) != null) System.out.println(showChats());
            else if ((matcher = Commands.getMatcher(input, Commands.CREATE_NEW_GROUP)) != null)
                System.out.println(createGroup(matcher));
            else if ((matcher = Commands.getMatcher(input, Commands.START_NEW_PRIVATE_CHAT)) != null)
                System.out.println(createPrivateChat(matcher));
            else if ((matcher = Commands.getMatcher(input, Commands.ENTER_CHAT)) != null) {
                String enterOutput = enterChat(matcher);
                System.out.println(enterOutput);
                if (enterOutput.equals("You have successfully entered the chat!")) new ChatMenu().run(scanner, chat);
            } else System.out.println("Invalid command!");
        }
    }

    private String showAllChannels() {
        int i = 0;
        String allChannels = "";
        allChannels += "All channels:";
        for (Channel channel : Messenger.getChannels())
            allChannels += "\n" + (++i) + ". " + channel.getName() + ", id: " + channel.getId() + ", members: " + channel.getMembers().size();
        return allChannels;
    }

    private String showChats() {
        String allChats = "";
        allChats += "Chats:";
        int i = 0;
        for (Chat chat : currentUser.getChats())
            allChats += "\n" + (++i) + ". " + chat.getName() + ", id: " + chat.getId() + ", " + getChatType(chat);
        return allChats;
    }

    private String enterChat(Matcher matcher) {
        String chatType = matcher.group("chatType");
        String id = matcher.group("id");
        switch (chatType) {
            case "group":
                chat = currentUser.getGroupById(id);
                break;
            case "channel":
                chat = currentUser.getChannelById(id);
                break;
            case "private chat":
                chat = currentUser.getPrivateChatById(id);
                break;
        }
        if (currentUser.getChats().contains(chat)) return "You have successfully entered the chat!";
        else return "You have no " + chatType + " with this id!";
    }

    private String createChannel(Matcher matcher) {
        String name = matcher.group("name");
        String id = matcher.group("id");
        if (isValidName(name))
            if (!channelIdExists(id)) {
                Channel addedChannel = new Channel(currentUser, id, name);
                Messenger.addChannel(addedChannel);
                moveToFirst(currentUser.getChats(), addedChannel);
                return "Channel " + matcher.group("name") + " has been created successfully!";
            } else return "A channel with this id already exists!";
        else return "Channel name's format is invalid!";
    }

    private String createGroup(Matcher matcher) {
        String name = matcher.group("name");
        String id = matcher.group("id");
        if (isValidName(name))
            if (!groupIdExists(id)) {
                Group group = new Group(currentUser, id, name);
                Messenger.addGroup(group);
                moveToFirst(currentUser.getChats(), group);
                return "Group " + matcher.group("name") + " has been created successfully!";
            } else return "A group with this id already exists!";
        else return "Group name's format is invalid!";
    }

    private String createPrivateChat(Matcher matcher) {
        String id = matcher.group("id");
        User addedToPrivateChat = Messenger.getUserById(id);
        if (addedToPrivateChat != null)
            if (currentUser.getPrivateChatById(id) == null) {
                PrivateChat pv = new PrivateChat(currentUser, addedToPrivateChat.getId(), addedToPrivateChat.getName());
                pv.addMember(addedToPrivateChat);
                if (currentUser != addedToPrivateChat) addedToPrivateChat.addPrivateChat(pv);
                moveToFirst(currentUser.getChats(), pv);
                moveToFirst(addedToPrivateChat.getChats(), pv);
                return "Private chat with " + addedToPrivateChat.getName() + " has been started successfully!";
            } else return "You already have a private chat with this user!";
        else return "No user with this id exists!";
    }

    private String joinChannel(Matcher matcher) {
        Channel channelToJoin = Messenger.getChannelById(matcher.group("id"));
        if (!isAlreadyInChat(currentUser, channelToJoin))
            if (channelToJoin != null) {
                currentUser.addChannel(channelToJoin);
                moveToFirst(currentUser.getChats(), channelToJoin);
                channelToJoin.getMembers().add(currentUser);
                return "You have successfully joined the channel!";
            } else return "No channel with this id exists!";
        else return "You're already a member of this channel!";
    }

    private String getChatType(Chat chat) {
        if (chat instanceof Group)
            return "group";
        else if (chat instanceof Channel)
            return "channel";
        else if (chat instanceof PrivateChat)
            return "private chat";
        return null;
    }

    private boolean isValidName(String name) {
        return Pattern.compile("\\w+").matcher(name).matches();
    }

    private boolean channelIdExists(String id) {
        return Messenger.getChannelById(id) != null;
    }

    private boolean groupIdExists(String id) {
        return Messenger.getGroupById(id) != null;
    }

    private boolean isAlreadyInChat(User user, Chat chat) {
        return user.getChats().contains(chat);
    }

    private void moveToFirst(ArrayList<Chat> chats, Chat chat) {
        chats.remove(chat);
        chats.add(0, chat);
    }
}