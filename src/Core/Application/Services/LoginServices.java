package Core.Application.Services;

import javax.swing.*;

public class LoginServices {


    public static String loginValidations(String dni,String password) {
        String message = "";
        if (dni.isEmpty() || password.isEmpty()) {
            return message= "Please fill all fields";
        }
        // Verificar que todos los caracteres, excepto el último, sean dígitos
        for (int i = 0; i < dni.length(); i++) {
            if (!Character.isDigit(dni.charAt(i))) {
                return "DNI must contain only numeric values";
            }
        }
        if (dni.length() < 6) {
            return message = "DNI must have at least 6 characters";
        }

        if (password.length() < 4) {
            return message ="Password must have at least 4 characters";
        }

        return message;
    }
}
