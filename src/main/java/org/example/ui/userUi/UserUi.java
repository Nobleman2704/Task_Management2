package org.example.ui.userUi;

import org.example.model.user.UserRole;
import org.example.service.user.UserServiceImpl;

import java.util.UUID;

import static org.example.ui.scannerUtil.ScannerUtil.SCANNER_STR;

public class UserUi {
    public static void userWindow(UUID userId) {
        UserServiceImpl userService = new UserServiceImpl();
        UserRole role = userService.getById(userId).getRole();

        if (role.equals(UserRole.MANAGER)) ManagerUi.managerWindow(userId);

        else if (role.equals(UserRole.BUSINESS_ANALYST)
                || role.equals(UserRole.BE_LEAD)
                || role.equals(UserRole.FE_LEAD)) {
            while (true) {
                String choiceForWindow = chooseWidow();
                switch (choiceForWindow) {
                    case "1" -> BA_BE_FE_UI.BA_BE_FE_Window(userId);
                    case "2" -> OtherUi.OtherWindow(userId);
                    case "0" -> {
                        return;
                    }
                }
            }
        } else if (!role.equals(UserRole.USER)) OtherUi.OtherWindow(userId);

        else System.out.println("Your role is user, so please wait manager's response");
    }

    public static String chooseWidow() {
        System.out.println("""
                \nDo you want to enter into task creator or task performer window?
                1. Task creator
                2. Task performer
                0. back""");
        return SCANNER_STR.nextLine();
    }
}
