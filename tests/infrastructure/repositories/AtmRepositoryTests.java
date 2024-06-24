package infrastructure.repositories;

import core.domain.Atm;
import core.domain.TransactionType;
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

public class AtmRepositoryTests {
    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;


    @BeforeEach
    public void setUp()  {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    public void testGetAtms() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true, false);
            when(mockResultSet.getInt("id")).thenReturn(1);
            when(mockResultSet.getInt("balance")).thenReturn(1000);

            List<Atm> atms = AtmRepository.getAtms();

            assertNotNull(atms);
            assertEquals(1, atms.size());
            assertEquals(1, atms.getFirst().getAtmId());
            assertEquals(1000, atms.getFirst().getBalance());
        }
    }

    @Test
    public void testUpdateBalance() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            AtmRepository.updateBalance(1, 500, TransactionType.D);

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).setInt(1, 500);
            verify(mockPreparedStatement, times(1)).setInt(2, 1);
        }
    }

    @Test
    public void testCreateNewAtm() throws SQLException {
        try (MockedStatic<DbConn> dbConnMockedStatic = Mockito.mockStatic(DbConn.class)) {
            dbConnMockedStatic.when(DbConn::getInstance).thenReturn(mock(DbConn.class));
            when(DbConn.getInstance().getConnection()).thenReturn(mockConnection);

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenReturn(1);

            AtmRepository.CreateNewAtm();

            verify(mockPreparedStatement, times(1)).executeUpdate();
            verify(mockPreparedStatement, times(1)).setInt(1, 10000);
        }
    }
}
