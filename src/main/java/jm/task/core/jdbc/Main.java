package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl service = new UserServiceImpl();
        service.createUsersTable();
        for (int i = 0; i < 4; i++) {
            String name = i + "test";
            service.saveUser(name, i * i + "name", (byte)i);
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        }
        service.getAllUsers().forEach(System.out::println);
        service.cleanUsersTable();
        service.dropUsersTable();
    }
}
