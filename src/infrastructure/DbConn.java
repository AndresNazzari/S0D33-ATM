package infrastructure;

import core.application.services.PasswordService;
import infrastructure.repositories.AccountRepository;
import infrastructure.repositories.UserRepository;

import java.sql.*;

public class DbConn {
    private static DbConn instance = null;

    private DbConn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver not found", e);
        }
    }

    public static synchronized DbConn getInstance() {
        if (instance == null) {
            instance = new DbConn();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/s0d33-atm";
            String username = "root";
            String password = "";
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    // Create the admin user if it doesn't exist
    // This is a one-time operation
    public static void createAdminUser() throws SQLException {
        String dni = "33028540";
        String firstName = "Andres";
        String lastName = "Nazzari";
        String password = PasswordService.hashPassword("33028540");
        boolean isAdmin = true;
        int initialFounds = 5000;

        Connection conn = getInstance().getConnection();
        PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE dni = ?");
        checkStmt.setString(1, dni);
        ResultSet rs = checkStmt.executeQuery();
        try {
            if (rs.next()) {
                System.out.println("Admin user already exists");
            } else {
                try  {
                    int id = UserRepository.createUser(firstName, lastName, dni, password, isAdmin);
                    AccountRepository.createAccount(id, initialFounds);

                    System.out.print("Admin user Created. DNI: " + dni + " Password: 33028540");
                } catch (SQLException e) {
                    throw new RuntimeException("Error creating admin user", e);
                }
            }
        } finally {
            if (rs != null) rs.close();
            checkStmt.close();
        }
    }
}
