package infrastructure.repositories;

import core.application.services.PasswordService;
import core.domain.User;
import infrastructure.DbConn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserRepositoryTest {
    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testCreateUser() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class);
            MockedStatic<PasswordService> passwordServiceMockedStatic = Mockito.mockStatic(PasswordService.class)) {

            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            String hashedPassword = "hashedPassword";
            passwordServiceMockedStatic.when(() -> PasswordService.hashPassword(anyString())).thenReturn(hashedPassword);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);
            when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(1);

            int userId = UserRepository.createUser("John", "Doe", "12345678", "password", true);

            assertEquals(1, userId);

            verify(mockPreparedStatement, times(1)).setString(1, "John");
            verify(mockPreparedStatement, times(1)).setString(2, "Doe");
            verify(mockPreparedStatement, times(1)).setString(3, "12345678");
            verify(mockPreparedStatement, times(1)).setString(4, hashedPassword);
            verify(mockPreparedStatement, times(1)).setBoolean(5, true);
            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }

    @Test
    public void testGetUserByDni() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getString("first_name")).thenReturn("John");
            when(mockResultSet.getString("last_name")).thenReturn("Doe");
            when(mockResultSet.getString("dni")).thenReturn("12345678");
            when(mockResultSet.getBoolean("is_admin")).thenReturn(true);
            when(mockResultSet.getString("password")).thenReturn("hashedPassword");
            when(mockResultSet.getInt("account_id")).thenReturn(1);
            when(mockResultSet.getInt("balance")).thenReturn(1000);

            User user = UserRepository.getUserByDni("12345678");

            assertNotNull(user);
            assertEquals(1, user.getUserId());
            assertEquals("John", user.getFirstName());
            assertEquals("Doe", user.getLastName());
            assertEquals("12345678", user.getDni());
            assertEquals(true, user.isAdmin());
            assertEquals("hashedPassword", user.getPassword());
            assertEquals(1, user.getAccountId());
            assertEquals(1000, user.getBalance());

            verify(mockPreparedStatement, times(1)).setString(1, "12345678");
        }
    }

    @Test
    public void testGetUserByDniNotFound() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            User user = UserRepository.getUserByDni("12345678");

            assertNull(user);

            verify(mockPreparedStatement, times(1)).setString(1, "12345678");
        }
    }
}