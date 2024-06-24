package infrastructure.repositories;

import core.domain.TransactionType;
import infrastructure.DbConn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AccountRepositoryTest {
    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testCreateAccount() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            AccountRepository.createAccount(1, 1000);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).setInt(1, 1);
            verify(mockPreparedStatement, times(1)).setInt(2, 1000);
        }
    }

    @Test
    public void testUpdateMyBalance() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            AccountRepository.updateMyBalance(500, 1, 1, TransactionType.D);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).setInt(1, 500);
            verify(mockPreparedStatement, times(1)).setInt(2, 1);
            verify(mockPreparedStatement, times(1)).setInt(3, 1);
        }
    }

    @Test
    public void testUpdateRecevierBalance() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            AccountRepository.updateRecevierBalance(500, 2);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).setInt(1, 500);
            verify(mockPreparedStatement, times(1)).setInt(2, 2);
        }
    }

    @Test
    public void testCreateAccountThrowsSQLException() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(0);

            assertThrows(SQLException.class, () -> AccountRepository.createAccount(1, 1000));

            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }

    @Test
    public void testUpdateMyBalanceThrowsSQLException() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(0);

            assertThrows(SQLException.class, () -> AccountRepository.updateMyBalance(500, 1, 1, TransactionType.D));

            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }

    @Test
    public void testUpdateRecevierBalanceThrowsSQLException() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(0);

            assertThrows(SQLException.class, () -> AccountRepository.updateRecevierBalance(500, 2));

            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }
}