package fr.ocatteau.dao;

import fr.ocatteau.api.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * User {@link AbstractDAO}.
 */
public class UserDao extends AbstractDAO<User> {

    /**
     * Constructor.
     * @param sessionFactory the {@link SessionFactory}
     */
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Find user by ID.
     * @param id the IDof the {@link User}
     * @return the {@link User}
     */
    public User findById(int id) {
        return currentSession().get(User.class, id);
    }

    /**
     * Find user by ID.
     * @param id the IDof the {@link User}
     * @return the {@link User}
     */
    public User findByLogin(String login) {
        return (User) currentSession().createQuery("from User where login = :login").setParameter("login", login).uniqueResult();
    }
}
