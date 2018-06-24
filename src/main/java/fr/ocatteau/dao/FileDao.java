package fr.ocatteau.dao;

import fr.ocatteau.api.ManagedFile;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * File {@link AbstractDAO}.
 */
public class FileDao extends AbstractDAO<ManagedFile> {

    /**
     * Constructor.
     * @param sessionFactory the {@link SessionFactory}
     */
    public FileDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Find all {@link ManagedFile}s.
     * @return the list of {@link ManagedFile}s
     */
    public List<ManagedFile> findAll() {
        return (List<ManagedFile>) currentSession().createCriteria(ManagedFile.class).list();
    }

    /**
     * Find {@link ManagedFile} by ID.
     * @param id the {@link ManagedFile} ID
     * @return the {@link ManagedFile}
     */
    public ManagedFile findById(int id) {
        return currentSession().get(ManagedFile.class, id);
    }

    /**
     * Insert a {@link ManagedFile}.
     * @param managedFile the {@link ManagedFile} to insert
     * @return the persisted {@link ManagedFile} entity
     */
    public ManagedFile insert(ManagedFile managedFile) {
        return persist(managedFile);
    }

    /**
     * Delete the {@link ManagedFile}.
     * @param managedFile the {@link ManagedFile} to delete
     */
    public void delete(ManagedFile managedFile) {
        currentSession().delete(managedFile);
    }
}
