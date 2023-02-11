package org.example.ui;

import static org.example.ui.scannerUtil.ScannerUtil.SCANNER_STR;

public class MainUi {
    public static void main() {
        while (true) {
            String choose = chooseOperation();
            switch (choose) {
                case "1" -> AuthUi.signUp();
                case "2" -> AuthUi.signIn();
                case "0" -> {
                    return;
                }
            }
        }
    }

    public static String chooseOperation() {
        System.out.println("""
                \n1. Sign up
                2. Sign in
                0. Exit
                Choose operation :""");
        return SCANNER_STR.nextLine();
    }
}
