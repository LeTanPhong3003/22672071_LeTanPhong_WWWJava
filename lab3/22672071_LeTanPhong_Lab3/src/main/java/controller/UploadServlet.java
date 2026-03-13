package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(name = "UploadServlet", urlPatterns = {"/uploads/*"})
public class UploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR_PARAM = "uploadDir";
    private static final String UPLOAD_DIR_PROPERTY = "product.upload.dir";
    private static final String DEFAULT_UPLOAD_DIR_NAME = "22672071-product-uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String fileName = Paths.get(pathInfo).getFileName().toString();
        Path uploadDir = resolveUploadDirectory(request.getServletContext());
        Path filePath = uploadDir.resolve(fileName).normalize();

        if (!filePath.startsWith(uploadDir) || !Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        response.setContentType(contentType);
        response.setHeader("Cache-Control", "public, max-age=86400");
        response.setContentLengthLong(Files.size(filePath));

        try (InputStream in = Files.newInputStream(filePath);
             OutputStream out = response.getOutputStream()) {
            in.transferTo(out);
        }
    }

    private Path resolveUploadDirectory(ServletContext servletContext) throws IOException {
        String configured = servletContext.getInitParameter(UPLOAD_DIR_PARAM);
        if (configured == null || configured.trim().isEmpty()) {
            configured = System.getProperty(UPLOAD_DIR_PROPERTY);
        }
        if (configured == null || configured.trim().isEmpty()) {
            configured = Paths.get(System.getProperty("user.home"), DEFAULT_UPLOAD_DIR_NAME).toString();
        }

        Path uploadDir = Paths.get(configured).toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);
        return uploadDir;
    }
}


