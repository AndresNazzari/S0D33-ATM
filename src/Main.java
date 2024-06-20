import Infrastructure.DbConn;
import Infrastructure.Repositories.UserRepository;
import javax.swing.*;
import Presentation.Views.LoginForm;
import Core.Application.Services.PasswordService;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.print("Hello and welcome!");

        // Este usuario solo se crea si no existe el usuario con dni 33028540
        DbConn.createAdminUser();

        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
        loginForm.setLocationRelativeTo(null); // Centrar la ventana
    }
}