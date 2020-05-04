package util;

import java.sql.*;

public class Database {

    private static final String DB_URL = "jdbc:mysql://localhost/supermarket";
    private static final String USER = "user";
    private static final String PASS = "password";

    private Connection connection;
    public PreparedStatement ps = null;
    public String query;
    public ResultSet rs;

    public Database() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
