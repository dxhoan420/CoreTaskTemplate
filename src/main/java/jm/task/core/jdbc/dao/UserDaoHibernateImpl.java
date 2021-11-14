package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final Session session;
    private CriteriaBuilder builder;
    private Transaction transaction;
    private final String tableName = "user";

    public UserDaoHibernateImpl() {
        session = Util.getSessionFactory().openSession();//Почему здесь не работает getCurrentSession?
    }

    private void createQueryAndExecute(String sql) {
        transaction = session.beginTransaction();
        Query query = session.createSQLQuery(sql).addEntity(User.class);
        query.executeUpdate();
        transaction.commit();
    }

    @Override
    public void createUsersTable() {
        String sql = "create table if not exists "
                + tableName +
                " (id bigint not null primary key," +
                " age tinyint null," +
                " lastName varchar(255) null," +
                " name varchar(255) null);";
        createQueryAndExecute(sql);
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        createQueryAndExecute(sql);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        transaction = session.beginTransaction();
        session.save(new User(name, lastName, age));
        transaction.commit();
    }

    @Override
    public void removeUserById(long id) {
        transaction = session.beginTransaction();
        //Hibernate
        User forRemoving = session.get(User.class, id);
        //JPA
//        User forRemoving = session.find(User.class, id);
        if (forRemoving != null)
            session.remove(forRemoving);
        transaction.commit();
    }

    @Override
    public List<User> getAllUsers() {
        builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        return session.createQuery(query).list();
//        return session.createQuery("from User", User.class).list();
    }

    @Override
    public void cleanUsersTable() {
        String sql = "truncate table " + tableName;
        createQueryAndExecute(sql);
    }
}
