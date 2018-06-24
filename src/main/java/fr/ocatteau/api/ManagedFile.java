package fr.ocatteau.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Objects;

@Entity
@Table(name = "FILE")
public class ManagedFile {

    @Id
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(name="file_seq", sequenceName="auto_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_seq")
    @NotNull
    @JsonProperty
    private Integer id;

    @Column(name = "FILENAME", length = 100, nullable = false)
    @NotNull
    @JsonProperty
    private String filename;

    /**
     * Empty constructor.
     */
    public ManagedFile() {
    }

    /**
     * Constructor from {@link File}.
     * @param file the input {@link File}
     */
    public ManagedFile(File file) {
        filename = file.getName();
    }

    public Integer getId() {
        return id;
    }

    public ManagedFile setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public ManagedFile setFilename(String filename) {
        this.filename = filename;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ManagedFile managedFile = (ManagedFile) o;
        return Objects.equals(id, managedFile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filename);
    }

    @Override
    public String toString() {
        return "File{ id=" + id + ", filename='" + filename + "\'" + "}";
    }
}