package View;

import Model.Messenger;
import Model.User;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu {
    private User loggedInUser = null;

    public void run(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            Matcher matcher;
            if (input.equals("exit"))
                break;
            else if ((matcher = Commands.getMatcher(input, Commands.REGISTER)) != null)
                System.out.println(register(matcher));
            else if ((matcher = Commands.getMatcher(input, Commands.LOGIN)) != null) {
                String loginOutput = login(matcher);
                System.out.println(login(matcher));
                if (loginOutput.equals("User successfully logged in!")) {
                    Messenger.setCurrentUser(loggedInUser);
                    new MessengerMenu().run(scanner);
                }
            } else System.out.println("Invalid command!");
        }
    }

    private String login(Matcher matcher) {
        loggedInUser = Messenger.getUserById(matcher.group("id"));
        String password = matcher.group("password");
        if (loggedInUser != null)
            if (isCorrectPassword(loggedInUser, password))
                return "User successfully logged in!";
            else return "Incorrect password!";
        else return "No user with this id exists!";
    }

    private String register(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String id = matcher.group("id");
        if (isValidUsername(username))
            if (isStrongPassword(password))
                if (!userAlreadyExist(id)) {
                    Messenger.addUser(new User(id, username, password));
                    return "User has been created successfully!";
                } else return "A user with this ID already exists!";
            else return "Password is weak!";
        else return "Username's format is invalid!";
    }

    private boolean isValidUsername(String username) {
        return Pattern.compile("\\w+").matcher(username).matches();
    }

    private boolean isStrongPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[*.!@$%^&(){}\\[\\]:;<>,?/~_+\\-=|])(?=.*[0-9])(?=.*[a-z]).{8,32}$");
        return pattern.matcher(password).matches();
    }

    private boolean userAlreadyExist(String id) {
        return Messenger.getUserById(id) != null;
    }

    private boolean isCorrectPassword(User user, String password) {
        return user.getPassword().equals(password);
    }
}
