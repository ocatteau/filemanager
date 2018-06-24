package fr.ocatteau.views;

import fr.ocatteau.api.ManagedFile;
import fr.ocatteau.api.User;
import io.dropwizard.views.View;

import java.util.List;

public class MainView extends View {

    private final User currentUser;
    private List<ManagedFile> managedFiles;


    public MainView(User currentUser, List<ManagedFile> managedFiles) {
        super("main.mustache");
        this.currentUser = currentUser;
        this.managedFiles = managedFiles;
    }

    /**
     * Return the current {@link USer}
     * @return
     */
    public String getCurrentUser() {
        return currentUser.getLogin();
    }

    /**
     * Return the list of {@link ManagedFile}s.
     * @return the list of {@link ManagedFile}s.
     */
    public List<ManagedFile> getManagedFiles() {
        return managedFiles;
    }
}
