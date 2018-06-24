package fr.ocatteau.views;

import io.dropwizard.views.View;

/**
 * Login {@link View}.
 */
public class LoginView extends View {
    /**
     * Constructor.
     */
    public LoginView() {
        super("login.mustache");
    }
}
