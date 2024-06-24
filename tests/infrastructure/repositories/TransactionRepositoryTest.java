package infrastructure.repositories;

import core.application.services.UserSession;
import core.domain.Transaction;
import core.domain.TransactionType;
import infrastructure.DbConn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TransactionRepositoryTest {
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
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testGetTransactionsAsAdmin() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class);
            MockedStatic<UserSession> userSessionMockedStatic = Mockito.mockStatic(UserSession.class)) {

            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            UserSession mockUserSession = mock(UserSession.class);
            userSessionMockedStatic.when(UserSession::getInstance).thenReturn(mockUserSession);
            when(mockUserSession.isAdmin()).thenReturn(true);

            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getInt("amount")).thenReturn(100);
            when(mockResultSet.getString("type")).thenReturn("D");
            when(mockResultSet.getTimestamp("created_at")).thenReturn(new java.sql.Timestamp(System.currentTimeMillis()));
            when(mockResultSet.getInt("atm_id")).thenReturn(1);

            List<Transaction> transactions = TransactionRepository.getTransactions(mockUserSession);

            assertNotNull(transactions);
            assertEquals(1, transactions.size());
            assertEquals(1, transactions.getFirst().getTransactionId());
            assertEquals(100, transactions.getFirst().getAmount());
        }
    }

    @Test
    public void testGetTransactionsAsUser() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class);
            MockedStatic<UserSession> userSessionMockedStatic = Mockito.mockStatic(UserSession.class)) {

            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            UserSession mockUserSession = mock(UserSession.class);
            userSessionMockedStatic.when(UserSession::getInstance).thenReturn(mockUserSession);
            when(mockUserSession.isAdmin()).thenReturn(false);
            when(mockUserSession.getAccountId()).thenReturn(1);

            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getInt("amount")).thenReturn(100);
            when(mockResultSet.getString("type")).thenReturn("D");
            when(mockResultSet.getTimestamp("created_at")).thenReturn(new java.sql.Timestamp(System.currentTimeMillis()));
            when(mockResultSet.getInt("atm_id")).thenReturn(1);

            List<Transaction> transactions = TransactionRepository.getTransactions(mockUserSession);

            assertNotNull(transactions);
            assertEquals(1, transactions.size());
            assertEquals(1, transactions.getFirst().getTransactionId());
            assertEquals(100, transactions.getFirst().getAmount());

            verify(mockPreparedStatement, times(1)).setInt(1, 1);
        }
    }

    @Test
    public void testCreateTransaction() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {

            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            TransactionRepository.createTransaction(1, 100, 1, TransactionType.D);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).setInt(1, 100);
            verify(mockPreparedStatement, times(1)).setInt(2, 1);
            verify(mockPreparedStatement, times(1)).setInt(3, 1);
            verify(mockPreparedStatement, times(1)).setString(4, "D");
        }
    }

    @Test
    public void testCreateTransactionTransfer() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {

            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            TransactionRepository.createTransaction(0, 100, 1, TransactionType.T);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).setInt(1, 100);
            verify(mockPreparedStatement, times(1)).setNull(2, Types.NULL);
            verify(mockPreparedStatement, times(1)).setInt(3, 1);
            verify(mockPreparedStatement, times(1)).setString(4, "T");
        }
    }

    @Test
    public void testCreateTransactionFails() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {

            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeUpdate()).thenReturn(0);

            assertThrows(SQLException.class, () -> TransactionRepository.createTransaction(1, 100, 1, TransactionType.D));

            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }
}