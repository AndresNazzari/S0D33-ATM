import Presentation.views.LoginForm;
import infrastructure.DbConn;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.print("Hello and welcome! /n");

        DbConn.createAdminUser();

        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
        loginForm.setLocationRelativeTo(null);
    }
}