package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.*;

public class UserDaoJDBCImpl implements UserDao {
    private final String tableName = "users";
    private Connection connection;
    private Statement statement;

    public UserDaoJDBCImpl() {
        try {
            connection = Util.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean tableExists(Connection connection) throws SQLException {
        ResultSet resultSet = connection.getMetaData().getTables(null, null, tableName, null);
        return resultSet.next()
                && resultSet.getString(3).equals(tableName);
    }

    private void executeAndClose(String sql, Statement statement, Connection connection) throws SQLException {
        if (sql != null) {
            statement.executeUpdate(sql);
        }
        statement.close();
        connection.close();
    }

    public void createUsersTable() {
        final String sql = "CREATE TABLE IF NOT EXISTS " + tableName +" (\n" +
                "  `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(45) NOT NULL,\n" +
                "  `lastName` varchar(45) NOT NULL,\n" +
                "  `age` tinyint NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;";
        try {
            executeAndClose(sql, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        final String sql = "DROP TABLE IF EXISTS " + tableName;
        try {
            executeAndClose(sql, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
//        Почему нельзя вместо users использовать знак вопроса?
        final String sql = "insert into users(name, lastName, age) values (?, ?, ?)";
        try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
//            prepareStatement.setString(1, tableName);
            prepareStatement.setString(1, name);
            prepareStatement.setString(2, lastName);
            prepareStatement.setString(3, Byte.toString(age));
            prepareStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        final String sql = "delete from " + tableName + " where id = " + id;
        try {
            executeAndClose(sql, statement, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        final String sql = "select * from " + tableName;
        List<User> userList = new ArrayList<>();
        try {
            if (!tableExists(connection)) {
                return userList;
            }
            ResultSet set = statement.executeQuery(sql);
            while (set.next()) {
                User user = new User();
                user.setId(set.getLong(1));
                user.setName(set.getString(2));
                user.setLastName(set.getString(3));
                user.setAge(set.getByte(4));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void cleanUsersTable() {
//        Лучше удалить все элементы так?
//        final String sql = "DELETE FROM users";
//        Или лучше сначала удалить таблицу,
//        final String sql = "DROP TABLE users";
        final String sql = "truncate table " + tableName;
        try {
            executeAndClose(sql, statement, connection);
//            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        а потом заново создать её?
//        createUsersTable();
    }
}
