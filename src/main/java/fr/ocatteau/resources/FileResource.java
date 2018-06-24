package fr.ocatteau.resources;


import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import fr.ocatteau.FileManagerConfiguration;
import fr.ocatteau.api.ManagedFile;
import fr.ocatteau.api.User;
import fr.ocatteau.dao.FileDao;
import fr.ocatteau.dao.UserDao;
import fr.ocatteau.views.MainView;
import io.dropwizard.hibernate.UnitOfWork;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;

import static com.codahale.metrics.MetricRegistry.name;

@Path("/filemanager")
@Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
public class FileResource {
    private FileManagerConfiguration configuration;
    private final Meter requests;
    private final Timer responses;
    private final FileDao fileDao;
    private final UserDao userDao;
    private EncryptionManager encryptionManager = new EncryptionManager("This is a secret");


    /**
     * Constructor.
     * @param configuration the {@link FileManagerConfiguration}
     * @param metrics the {@link MetricRegistry}
     * @param fileDao the {@link FileDao}
     * @param userDao the {@link UserDao}
     */
    public FileResource(FileManagerConfiguration configuration, MetricRegistry metrics, FileDao fileDao, UserDao userDao) {
        this.configuration = configuration;
        requests = metrics.meter("requests");
        responses = metrics.timer(name(FileResource.class, "responses"));
        this.fileDao = fileDao;
        this.userDao = userDao;
    }


    @GET
    @Path("/{login}")
    @UnitOfWork(readOnly = true)
    public MainView mainView(@PathParam("login") String login) {
        requests.mark();
        final Timer.Context context = responses.time();

        User user = userDao.findByLogin(login);
        List<ManagedFile> managedFiles = fileDao.findAll();

        try {
            return new MainView(user, managedFiles);
        } finally {
            context.stop();
        }

    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @UnitOfWork
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
        String uploadedFileLocation = configuration.getTempDirectory() + fileDetail.getFileName();
        File uploadedFile = writeToFile(uploadedInputStream, uploadedFileLocation);
        File encryptedFile = encryptedFile(fileDetail.getFileName());
        encryptionManager.encrypt(uploadedFile, encryptedFile);

        ManagedFile managedFile = fileDao.insert(new ManagedFile(encryptedFile));
        File renamedFile = new File(encryptedFile.getParent(), encryptedFileName(managedFile));
        encryptedFile.renameTo(renamedFile);

        uploadedFile.delete();

        List<ManagedFile> allFiles = fileDao.findAll();
        return Response.ok(allFiles).build();
    }

    @GET
    @Path("/file/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @UnitOfWork(readOnly = true)
    public Response getFile(@PathParam("id") Integer fileId) throws Exception {
        ManagedFile managedFile = fileDao.findById(fileId);
        File encryptedFile = encryptedFile(encryptedFileName(managedFile));
        File decryptedFile = new File(configuration.getTempDirectory(), managedFile.getFilename());
        encryptionManager.decrypt(encryptedFile, decryptedFile);

        try {
            return Response.ok(new FileInputStream(decryptedFile))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + decryptedFile.getName() + "\"")
                    .build();
        } finally {
            decryptedFile.delete();
        }
    }

    private File encryptedFile(String fileName) {
        return new File(configuration.getEncryptedDirectory(), fileName);
    }

    private String encryptedFileName(ManagedFile managedFile) {
        return managedFile.getId() + "-" + managedFile.getFilename();
    }


    // save uploaded file to new location
    private File writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        int read;
        final int BUFFER_LENGTH = 1024;
        final byte[] buffer = new byte[BUFFER_LENGTH];
        File uploadedFile = new File(uploadedFileLocation);
        OutputStream out = null;
        try {
            out = new FileOutputStream(uploadedFile);
            while ((read = uploadedInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            return uploadedFile;
        } catch (IOException e) {
            // TODO : logger
            throw e;
        } finally {
            out.flush();
            out.close();
        }
    }

}
