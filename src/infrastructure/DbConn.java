package infrastructure;

import core.Application.services.PasswordService;

import java.sql.*;

public class DbConn {
    private static DbConn instance = null;
    public static Connection connection;

    private DbConn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/s0d33-atm";
            String username ="root";
            String password = "";

            connection = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public static synchronized DbConn getInstance() {
        if (instance == null) instance = new DbConn();
        return instance;
    }

   public Connection getConnection() {
        return connection;
    }

    public static void createAdminUser() throws SQLException {
        String dni = "33028540";
        String firstName = "Andres";
        String lastName = "Nazzari";
        String password = PasswordService.hashPassword("kuma");

        Connection conn = getInstance().getConnection();
        PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE dni = ?");
        checkStmt.setString(1, dni);
        ResultSet rs = checkStmt.executeQuery();

        try {
            if (rs.next()) {
                System.out.println("Admin user already exists");
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO users (first_name, last_name, dni, password, is_admin) VALUES (?, ?, ?, ?, ?)")) {
                    insertStmt.setString(1, firstName);
                    insertStmt.setString(2, lastName);
                    insertStmt.setString(3, dni);
                    insertStmt.setString(4, password);
                    insertStmt.setInt(5, 1);

                    insertStmt.executeUpdate();
                    System.out.print("Admin user Created. DNI: " + dni + " Password: kuma");
                }
            }
        } finally {
            if (rs != null) rs.close();
            checkStmt.close();
        }
    }
}
