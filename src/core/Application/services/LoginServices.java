package core.Application.services;

public class LoginServices {


    public static String loginValidations(String dni,String password) {
        String message = "";
        if (dni.isEmpty() || password.isEmpty()) {
            return "Please fill all fields";
        }

        for (int i = 0; i < dni.length(); i++) {
            if (!Character.isDigit(dni.charAt(i))) {
                return "DNI must contain only numeric values";
            }
        }
        if (dni.length() < 6) {
            return "DNI must have at least 6 characters";
        }

        if (password.length() < 4) {
            return "Password must have at least 4 characters";
        }

        return message;
    }
}
