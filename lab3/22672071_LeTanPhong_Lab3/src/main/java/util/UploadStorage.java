package util;

import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Resolves a persistent upload directory outside the deployed WAR.
 */
public final class UploadStorage {
    private static final String CONFIG_PARAM = "uploadDir";
    private static final String SYSTEM_PROPERTY = "product.upload.dir";
    private static final String DEFAULT_FOLDER = "22672071-product-uploads";

    private UploadStorage() {
    }

    public static Path resolveUploadDirectory(ServletContext servletContext) throws IOException {
        String configured = servletContext.getInitParameter(CONFIG_PARAM);
        if (configured == null || configured.trim().isEmpty()) {
            configured = System.getProperty(SYSTEM_PROPERTY);
        }
        if (configured == null || configured.trim().isEmpty()) {
            configured = Paths.get(System.getProperty("user.home"), DEFAULT_FOLDER).toString();
        }

        Path uploadDir = Paths.get(configured).toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);
        return uploadDir;
    }
}

