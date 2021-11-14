package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String url = "jdbc:mysql://localhost:3306/dbtest";
    private static final String user = "root";
    private static final String password = "SQLtest123";
    private static SessionFactory sessionFactory;

    public static Connection getConnection() throws SQLException {
        return getConnection(url, user, password);
    }

    public static Connection getConnection(String url, String login, String password) throws SQLException {
        return DriverManager.getConnection(url, login, password);
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration()
                    .addAnnotatedClass(jm.task.core.jdbc.model.User.class)
                    .setProperty(Environment.URL, url)
                    .setProperty(Environment.USER, user)
                    .setProperty(Environment.PASS, password)
                    .setProperty(Environment.SHOW_SQL, "true")
//                    .setProperty(Environment.DRIVER, "com.mysql.cj.jdbc.Driver")
//                    .setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect")
//                    .setProperty(Environment.DEFAULT_SCHEMA, "dbtest")
                    .setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread")
                    .setProperty(Environment.USE_NEW_ID_GENERATOR_MAPPINGS, "true")
                    .setProperty(Environment.HBM2DDL_AUTO, "create"); //validate, update, create, create-drop

//            ServiceRegistry registry = new StandardServiceRegistryBuilder()
//                    .applySettings(configuration.getProperties()).build();
//            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }

}
