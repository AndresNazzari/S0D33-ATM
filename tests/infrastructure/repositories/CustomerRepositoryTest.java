package infrastructure.repositories;

import core.application.services.UserSession;
import core.domain.Customer;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomerRepositoryTest {
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
    public void testGetCustomersIncludeMe() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getString("first_name")).thenReturn("John");
            when(mockResultSet.getString("last_name")).thenReturn("Doe");
            when(mockResultSet.getInt("dni")).thenReturn(12345678);
            when(mockResultSet.getInt("account_id")).thenReturn(1);
            when(mockResultSet.getInt("balance")).thenReturn(1000);

            List<Customer> customers = CustomerRepository.getCustomers(true);

            assertNotNull(customers);
            assertEquals(1, customers.size());
            assertEquals(1, customers.getFirst().getCustomerId());
            assertEquals("John", customers.getFirst().getFirstName());
            assertEquals("Doe", customers.getFirst().getLastName());
            assertEquals(12345678, customers.getFirst().getDni());
            assertEquals(1, customers.getFirst().getAccountId());
            assertEquals(1000, customers.getFirst().getBalance());
        }
    }
    @Test
    public void testGetCustomersExcludeMe() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class);
             MockedStatic<UserSession> userSessionMockedStatic = Mockito.mockStatic(UserSession.class)) {

            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            UserSession mockUserSession = mock(UserSession.class);
            userSessionMockedStatic.when(UserSession::getInstance).thenReturn(mockUserSession);
            when(mockUserSession.getUserId()).thenReturn(2);

            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getString("first_name")).thenReturn("John");
            when(mockResultSet.getString("last_name")).thenReturn("Doe");
            when(mockResultSet.getInt("dni")).thenReturn(12345678);
            when(mockResultSet.getInt("account_id")).thenReturn(1);
            when(mockResultSet.getInt("balance")).thenReturn(1000);

            List<Customer> customers = CustomerRepository.getCustomers(false);

            assertNotNull(customers);
            assertEquals(1, customers.size());
            assertEquals(1, customers.getFirst().getCustomerId());
            assertEquals("John", customers.getFirst().getFirstName());
            assertEquals("Doe", customers.getFirst().getLastName());
            assertEquals(12345678, customers.getFirst().getDni());
            assertEquals(1, customers.getFirst().getAccountId());
            assertEquals(1000, customers.getFirst().getBalance());

            verify(mockPreparedStatement, times(1)).setInt(1, 2);
        }
    }

}