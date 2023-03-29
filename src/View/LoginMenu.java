package View;

import Model.Messenger;
import Model.User;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginMenu {
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
                    Messenger.setCurrentUser(Messenger.getUserById(matcher.group("id")));
                    new MessengerMenu().run(scanner);
                }
            } else System.out.println("Invalid command!");
        }
    }

    private String login(Matcher matcher) {
        User loggedInUser = Messenger.getUserById(matcher.group("id"));
        if (loggedInUser != null)
            if (isCorrectPassword(loggedInUser, matcher.group("password")))
                return "User successfully logged in!";
            else return "Incorrect password!";
        else return "No user with this id exists!";
    }

    private String register(Matcher matcher) {
        if (isValidUsername(matcher.group("username")))
            if (isStrongPassword(matcher.group("password")))
                if (!userAlreadyExist(matcher.group("id"))) {
                    Messenger.addUser(new User(matcher.group("id"), matcher.group("username"), matcher.group("password")));
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
