package fr.ocatteau;

import com.codahale.metrics.MetricRegistry;
import fr.ocatteau.api.ManagedFile;
import fr.ocatteau.api.User;
import fr.ocatteau.dao.FileDao;
import fr.ocatteau.dao.UserDao;
import fr.ocatteau.resources.FileResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.hibernate.SessionFactory;


/**
 * FileManager {@link Application}.
 */
public class FileManagerApplication extends Application<FileManagerConfiguration> {
    private final MetricRegistry metrics = new MetricRegistry();
    private final HibernateBundle<FileManagerConfiguration> hibernateBundle = createHibernateBundle();

    /**
     * Main.
     * @param args arguments of the Main
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new FileManagerApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<FileManagerConfiguration> bootstrap) {
        bootstrap.addBundle(new FlywayBundle<FileManagerConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(FileManagerConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }

            @Override
            public FlywayFactory getFlywayFactory(FileManagerConfiguration configuration) {
                return configuration.getFlywayFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(new AssetsBundle("/assets", "/assets"));
    }

    @Override
    public void run(FileManagerConfiguration configuration, Environment environment) {
        SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
        environment.jersey().register(
                new FileResource(configuration, metrics, new FileDao(sessionFactory), new UserDao(sessionFactory)));
        environment.jersey().register(MultiPartFeature.class);
        }

    private HibernateBundle<FileManagerConfiguration> createHibernateBundle() {
        return new HibernateBundle<FileManagerConfiguration>(
                User.class,
                ManagedFile.class) {

            @Override
            public PooledDataSourceFactory getDataSourceFactory(FileManagerConfiguration fileManagerConfiguration) {
                return fileManagerConfiguration.getDataSourceFactory();
            }
        };
    }
}
