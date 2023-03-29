package View;

import Model.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessengerMenu {
    private Chat chat;
    private User currentUser;
    private String chatType;

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
                switch (matcher.group("chatType")) {
                    case "group":
                        chatType = "group";
                        chat = currentUser.getGroupById(matcher.group("id"));
                        break;
                    case "channel":
                        chatType = "channel";
                        chat = currentUser.getChannelById(matcher.group("id"));
                        break;
                    case "private chat":
                        chatType = "private chat";
                        chat = currentUser.getPrivateChatById(matcher.group("id"));
                        break;
                }
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
        if (currentUser.getChats().contains(chat)) return "You have successfully entered the chat!";
        else return "You have no " + chatType + " with this id!";
    }

    private String createChannel(Matcher matcher) {
        if (isValidName(matcher.group("name"))) if (!channelIdExists(matcher.group("id"))) {
            Channel addedChannel = new Channel(currentUser, matcher.group("id"), matcher.group("name"));
            Messenger.addChannel(addedChannel);
            currentUser.addChannel(addedChannel);
            moveToFirst(currentUser.getChats(), addedChannel);
            return "Channel " + matcher.group("name") + " has been created successfully!";
        } else return "A channel with this id already exists!";
        else return "Channel name's format is invalid!";
    }

    private String createGroup(Matcher matcher) {
        if (isValidName(matcher.group("name"))) if (!groupIdExists(matcher.group("id"))) {
            Group group = new Group(currentUser, matcher.group("id"), matcher.group("name"));
            Messenger.addGroup(group);
            currentUser.addGroup(group);
            moveToFirst(currentUser.getChats(), group);
            return "Group " + matcher.group("name") + " has been created successfully!";
        } else return "A group with this id already exists!";
        else return "Group name's format is invalid!";
    }

    private String createPrivateChat(Matcher matcher) {
        User addedToPrivateChat = Messenger.getUserById(matcher.group("id"));
        if (addedToPrivateChat != null) if (currentUser.getPrivateChatById(matcher.group("id")) == null) {
            PrivateChat pv = new PrivateChat(currentUser, addedToPrivateChat.getId(), addedToPrivateChat.getName());
            addedToPrivateChat.addPrivateChat(pv);
            pv.addMember(addedToPrivateChat);
            if (currentUser != addedToPrivateChat) currentUser.addPrivateChat(pv);
            moveToFirst(currentUser.getChats(), pv);
            moveToFirst(addedToPrivateChat.getChats(), pv);
            return "Private chat with " + addedToPrivateChat.getName() + " has been started successfully!";
        } else return "You already have a private chat with this user!";
        else return "No user with this id exists!";
    }

    private String joinChannel(Matcher matcher) {
        Channel channelToJoin = Messenger.getChannelById(matcher.group("id"));
        if (!isAlreadyInChat(currentUser, channelToJoin)) if (channelToJoin != null) {
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