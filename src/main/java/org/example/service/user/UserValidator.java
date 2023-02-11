package org.example.service.user;

public abstract class UserValidator {
    public static boolean checkMail(String mail) {
        if (mail == null) return true;
        return !mail.matches("\\w+@(gmail|yandex|outlook|yahoo)\\." +
                "(uz|ru|com|org)");
    }

    public static boolean checkPassword(String password) {
        if (password != null && password.length() > 7) {
            boolean matchesUpperLetter = password.matches(".*[A-Za-z]+.*");
            boolean matchesNumber = password.matches(".*\\d+.*");
            boolean matchesSymbols = password.matches(".*[!@#$%^&*()_?/,.<>+~`-]+.*");
            return !matchesSymbols || !matchesNumber || !matchesUpperLetter;
        }
        return true;
    }

    public static boolean checkName(String name) {
        return name == null || name.isBlank();
    }

    public static boolean checkLastName(String lastName) {
        return lastName == null || lastName.isBlank();
    }
}
