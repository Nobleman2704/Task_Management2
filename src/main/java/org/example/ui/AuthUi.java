package org.example.ui;

import org.example.dto.response.UserResponse;
import org.example.model.user.User;
import org.example.repository.UserRepository;
import org.example.ui.userUi.UserUi;

import static org.example.Main.userService;
import static org.example.ui.scannerUtil.ScannerUtil.SCANNER_STR;

public class AuthUi implements UserRepository {
    public static void signUp() {

        System.out.print("Write your name : ");
        String name = SCANNER_STR.nextLine();

        System.out.print("Write your last name : ");
        String lastName = SCANNER_STR.nextLine();

        System.out.print("Write your email : ");
        String email = SCANNER_STR.nextLine();

        System.out.print("Write your password : ");
        String password = SCANNER_STR.nextLine();

        User user = new User()
                .setName(name)
                .setLastname(lastName)
                .setPassword(password)
                .setEmail(email);

        UserResponse response = userService.signUp(user);
        System.out.println(response.getMessage());
    }

    public static void signIn() {
        System.out.print("Write your email : ");
        String email = SCANNER_STR.nextLine();

        System.out.print("Write your password : ");
        String password = SCANNER_STR.nextLine();

        UserResponse userResponse = userService.signIn(email, password);

        if (userResponse.getStatus() != 777) {
            System.out.println(userResponse.getMessage());
        } else {
            System.out.println(userResponse.getMessage());
            UserUi.userWindow(userResponse.getUserId());
        }
    }
}
