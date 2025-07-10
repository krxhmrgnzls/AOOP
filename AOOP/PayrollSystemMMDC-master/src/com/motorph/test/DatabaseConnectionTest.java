package com.motorph.test;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import com.motorph.util.DatabaseConnection;

public class DatabaseConnectionTest {
    
    @Test
    @DisplayName("Test Database Connection Establishment")
    void testConnectionEstablishment() {
        assertDoesNotThrow(() -> {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            Connection conn = dbConnection.getConnection();
            assertNotNull(conn, "Connection should not be null");
            assertFalse(conn.isClosed(), "Connection should be open");
        });
    }
    
    @Test
    @DisplayName("Test Database Connection URL")
    void testConnectionURL() {
        assertDoesNotThrow(() -> {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            Connection conn = dbConnection.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            String url = metaData.getURL();
            assertTrue(url.contains("aoop"), "Database URL should contain 'aoop'");
            assertTrue(url.contains("3307"), "Database URL should contain port 3307");
        });
    }
    
    @Test
    @DisplayName("Test Database Tables Existence")
    void testRequiredTablesExist() {
        assertDoesNotThrow(() -> {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            Connection conn = dbConnection.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            
            String[] requiredTables = {
                "employees", "attendance", "payroll", 
                "login_credentials", "leave_balances", 
                "leave_requests", "overtime_requests"
            };
            
            for (String tableName : requiredTables) {
                ResultSet rs = metaData.getTables(null, null, tableName, null);
                assertTrue(rs.next(), "Table '" + tableName + "' should exist");
                rs.close();
            }
        });
    }
}