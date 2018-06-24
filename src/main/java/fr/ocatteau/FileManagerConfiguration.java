package fr.ocatteau;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class FileManagerConfiguration extends Configuration {
    @NotEmpty
    private String tempDirectory;
    @NotEmpty
    private String encryptedDirectory;

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();


    @JsonProperty("tempDirectory")
    public String getTempDirectory() {
        return tempDirectory;
    }

    @JsonProperty("tempDirectory")
    public void setTempDirectory(String tempDirectory) {
        this.tempDirectory = tempDirectory;
    }

    @JsonProperty("encryptedDirectory")
    public String getEncryptedDirectory() {
        return encryptedDirectory;
    }

    @JsonProperty("encryptedDirectory")
    public void setEncryptedDirectory(String encryptedDirectory) {
        this.encryptedDirectory = encryptedDirectory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

    /**
     * Return a {@link FlywayFactory}.
     * @return a {@link FlywayFactory}.
     */
    public FlywayFactory getFlywayFactory() {
        return new FlywayFactory();
    }
}
