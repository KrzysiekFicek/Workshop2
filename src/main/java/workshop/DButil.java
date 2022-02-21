package workshop;

import java.sql.*;

public class DButil {
    public static final String SERVER_URL = "jdbc:mysql://localhost:3306";
    public static final String USER = "root";
    public static final String PASSWORD = "coderslab";

    public static void main(String[] args) {


    }

    public static Connection connect() throws SQLException {
        Connection connect = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
        return connect;
    }
// Połączenie do konkretnej bazy danych
    public static Connection connect(String dbName) throws SQLException {
        Connection connect = DriverManager.getConnection(SERVER_URL + "/" + dbName, USER, PASSWORD);
        return connect;
    }

    public static void insert(Connection conn, String query, String... params) {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCinema(String name, String address) throws SQLException {
        Connection connection = DButil.connect("cinemas2_ex");
        DButil.insert(connection, "INSERT INTO cinemas(name, address) VALUE (?,?)", name, address);
    }

    public static void printData(Connection conn, String query, String... columnNames) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                for (String columnName : columnNames) {
                    System.out.println(resultSet.getString(columnName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final String DELETE_QUERY = "DELETE FROM tableName where id = ?";
    public static void remove(Connection conn, String tableName, int id) {
        try (PreparedStatement statement =
                     conn.prepareStatement(DELETE_QUERY.replace("tableName", tableName))) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
