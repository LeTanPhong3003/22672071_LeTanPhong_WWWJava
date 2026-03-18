package controller;

import dao.ProductDAO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Servlet demonstrating ProductDAO usage with URL patterns
 * URL patterns: /new, /insert, /delete, /edit, /update, /list
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/new", "/insert", "/delete", "/edit", "/update", "/list"})
@MultipartConfig
public class ProductServlet extends HttpServlet {

    private static final String UPLOAD_DIR_PARAM = "uploadDir";
    private static final String UPLOAD_DIR_PROPERTY = "product.upload.dir";
    private static final String DEFAULT_UPLOAD_DIR_NAME = "22672071-product-uploads";

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        switch (path) {
            case "/list":
                listProducts(request, response);
                break;
            case "/new":
                showNewForm(request, response);
                break;
            case "/edit":
                showEditForm(request, response);
                break;
            case "/delete":
                deleteProduct(request, response);
                break;
            default:
                listProducts(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        switch (path) {
            case "/insert":
                insertProduct(request, response);
                break;
            case "/update":
                updateProduct(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/list");
                break;
        }
    }

    /**
     * /list - List all products (default)
     */
    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = trimToNull(request.getParameter("keyword"));
        Double minPrice = parsePriceParameter(request.getParameter("minPrice"));
        Double maxPrice = parsePriceParameter(request.getParameter("maxPrice"));

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            double temp = minPrice;
            minPrice = maxPrice;
            maxPrice = temp;
        }

        List<Product> products = productDAO.selectProductsByFilters(keyword, minPrice, maxPrice);
        request.setAttribute("products", products);
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.setAttribute("minPrice", minPrice);
        request.setAttribute("maxPrice", maxPrice);
        request.getRequestDispatcher("/views/product-list.jsp").forward(request, response);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Double parsePriceParameter(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            double parsed = Double.parseDouble(value.trim());
            return parsed < 0 ? null : parsed;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * /new - Show form to create new product
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/product-form.jsp").forward(request, response);
    }

    /**
     * /edit - Show form to edit existing product
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.selectProductById(id);

        if (product != null) {
            request.setAttribute("product", product);
            request.getRequestDispatcher("/views/product-form.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/list");
        }
    }

    /**
     * /insert - Insert new product (POST)
     */
    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        String urlImage = resolveImageSource(request, null);

        Product product = new Product(name, price, urlImage);
        boolean success = productDAO.insertProduct(product);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/list?message=created");
        } else {
            response.sendRedirect(request.getContextPath() + "/new?error=failed");
        }
    }

    /**
     * /update - Update existing product (POST)
     */
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));

        Product existingProduct = productDAO.selectProductById(id);
        String existingImage = existingProduct != null ? existingProduct.getUrlImage() : null;
        String urlImage = resolveImageSource(request, existingImage);

        Product product = new Product(id, name, price, urlImage);
        boolean success = productDAO.updateProduct(product);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/list?message=updated");
        } else {
            response.sendRedirect(request.getContextPath() + "/edit?id=" + id + "&error=failed");
        }
    }

    /**
     * /delete - Delete product (GET)
     */
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = productDAO.deleteProduct(id);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/list?message=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/list?error=failed");
        }
    }

    private String resolveImageSource(HttpServletRequest request, String existingImage)
            throws IOException, ServletException {
        Part imagePart = request.getPart("imageFile");

        if (hasFile(imagePart)) {
            return saveUploadedFile(request, imagePart);
        }

        String imageUrl = request.getParameter("urlImage");
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            return imageUrl.trim();
        }

        return existingImage;
    }

    private boolean hasFile(Part filePart) {
        return filePart != null && filePart.getSize() > 0
                && filePart.getSubmittedFileName() != null
                && !filePart.getSubmittedFileName().trim().isEmpty();
    }

    private String saveUploadedFile(HttpServletRequest request, Part filePart)
            throws IOException {
        String originalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String extension = getSafeExtension(originalName);
        String uniqueName = UUID.randomUUID() + extension;

        Path uploadDir = resolveUploadDirectory(request.getServletContext());
        Files.createDirectories(uploadDir);
        Path destination = uploadDir.resolve(uniqueName);

        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        return "uploads/" + uniqueName;
    }

    private Path resolveUploadDirectory(ServletContext servletContext) {
        String configured = servletContext.getInitParameter(UPLOAD_DIR_PARAM);
        if (configured == null || configured.trim().isEmpty()) {
            configured = System.getProperty(UPLOAD_DIR_PROPERTY);
        }
        if (configured == null || configured.trim().isEmpty()) {
            configured = Paths.get(System.getProperty("user.home"), DEFAULT_UPLOAD_DIR_NAME).toString();
        }
        return Paths.get(configured).toAbsolutePath().normalize();
    }

    private String getSafeExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return ".png";
        }

        String ext = fileName.substring(dotIndex).toLowerCase();
        switch (ext) {
            case ".jpg":
            case ".jpeg":
            case ".png":
            case ".gif":
            case ".webp":
                return ext;
            default:
                return ".png";
        }
    }
}
