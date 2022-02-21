package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import workshop.DButil;

import java.sql.*;
import java.util.Arrays;

public class UserDao {

    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES(?,?,?)";
    private static final String READ_USER_QUERY = "SELECT FROM users WHERE id = ?";
    private static final String UPDATE_USERS_QUERY = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
    private static final String DELETE_USERS_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String CREATE_DATA_BASE_QUERY = "CREATE DATABASE workshop2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS users(\n" +
            "    id INT AUTO_INCREMENT,\n" +
            "    email VARCHAR(255) UNIQUE,\n" +
            "    username VARCHAR(255),\n" +
            "    password VARCHAR(60),\n" +
            "    PRIMARY KEY (id));";

    //    DB utworzona w pliku.sql + QUERY skopiowane do atrybutu
    public void createDataBase() {
        try (Connection conn = DButil.connect("workshop2");) {
            conn.createStatement().executeUpdate(CREATE_DATA_BASE_QUERY);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // Tworzenie tabeli tak jak w przypadku bazy w pliku sql
    public void createTable() {
        try (Connection connection = DButil.connect("workshop2")) {
            connection.createStatement().executeUpdate(CREATE_TABLE_QUERY);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public User create(User user) {
        try (Connection conn = DButil.connect("workshop2")) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User read(int userId) {
        try (Connection conn = DButil.connect("workshop2")) {
            PreparedStatement statement = conn.prepareStatement(READ_USER_QUERY);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public  void update(User user) {
        try (Connection conn = DButil.connect("workshop2")) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_USERS_QUERY);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
//            statement.setString(3, this.hashPassword(user.getPassword()));
            statement.setInt(3, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public User[] findAllUsers(){
        try (Connection connection = DButil.connect("workshop2")) {
            User[] users = new User[0];
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_USERS_QUERY);
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                users = Arrays.copyOf(users, users.length +1);
                users[users.length -1] = user;
            }
            return users;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public void delete(int userId) {
        try (Connection conn = DButil.connect("workshop2")) {
            PreparedStatement statement = conn.prepareStatement(DELETE_USERS_QUERY);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}