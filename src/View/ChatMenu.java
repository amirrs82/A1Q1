package View;

import Model.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

public class ChatMenu {
    private Chat chat;
    private User currentUser;

    public void run(Scanner scanner, Chat chat) {
        this.chat = chat;
        currentUser = Messenger.getCurrentUser();
        while (true) {
            String input = scanner.nextLine();
            Matcher matcher;
            if (input.equals("back")) break;
            else if ((matcher = Commands.getMatcher(input, Commands.SEND_MESSAGE)) != null)
                System.out.println(sendMessage(matcher));
            else if ((matcher = Commands.getMatcher(input, Commands.ADD_MEMBER)) != null)
                System.out.println(addMember(matcher));
            else if (Commands.getMatcher(input, Commands.SHOW_ALL_MESSAGES) != null) System.out.println(showMessages());
            else if (Commands.getMatcher(input, Commands.SHOW_ALL_MEMBERS) != null) System.out.println(showMembers());
            else System.out.println("Invalid command!");
        }
    }


    private String showMessages() {
        String messages = "";
        messages += "Messages:";
        for (Message message : chat.getMessages()) {
            User messageOwner = message.getOwner();
            messages += "\n" + messageOwner.getName() + "(" + messageOwner.getId() + "): \"" + message.getContent() + "\"";
        }
        return messages;
    }

    private String showMembers() {
        String members = "";
        if (chat instanceof PrivateChat) {
            members += "Invalid command!";
        } else {
            members += "Members:";
            for (User member : chat.getMembers()) {
                if (isOwner(member)) members += "\nname: " + member.getName() + ", id: " + member.getId() + " *owner";
                else members += "\nname: " + member.getName() + ", id: " + member.getId();
            }
        }
        return members;
    }

    private String addMember(Matcher matcher) {
        if (!isPrivateChat(chat)) if (isOwner(currentUser)) {
            User addedMember = Messenger.getUserById(matcher.group("id"));
            if (addedMember != null) {
                if (!isAlreadyInChat(addedMember)) {
                    addedMember.addChat(chat);
                    chat.addMember(addedMember);
                    moveToFirst(addedMember.getChats(), chat);
                    if (isGroup(chat)) {
                        chat.addMessage(new Message(currentUser, addedMember.getName() + " has been added to the group!"));
                        for (User member : chat.getMembers())
                            moveToFirst(member.getChats(), chat);
                    }
                    return "User has been added successfully!";
                } else return "This user is already in the chat!";
            } else return "No user with this id exists!";
        } else return "You don't have access to add a member!";
        else return "Invalid command!";
    }

    private String sendMessage(Matcher matcher) {
        if (hasAccessToSendMessage()) {
            String message = matcher.group("message");
            chat.addMessage(new Message(currentUser, message));
            for (User member : chat.getMembers())
                moveToFirst(member.getChats(), chat);
            return "Message has been sent successfully!";
        } else return "You don't have access to send a message!";
    }

    private boolean hasAccessToSendMessage() {
        return !(chat instanceof Channel) || chat.getOwner().equals(currentUser);
    }

    private boolean isPrivateChat(Chat chat) {
        return chat instanceof PrivateChat;
    }

    private boolean isGroup(Chat chat) {
        return chat instanceof Group;
    }

    private boolean isOwner(User user) {
        return chat.getOwner().equals(user);
    }

    private boolean isAlreadyInChat(User user) {
        return user.getChats().contains(chat);
    }

    private void moveToFirst(ArrayList<Chat> chats, Chat chat) {
        chats.remove(chat);
        chats.add(0, chat);
    }
}
