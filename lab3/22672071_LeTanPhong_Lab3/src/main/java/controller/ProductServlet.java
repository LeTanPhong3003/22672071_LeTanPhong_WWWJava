package controller;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

import java.io.IOException;
import java.util.List;

/**
 * Servlet demonstrating ProductDAO usage with URL patterns
 * URL patterns: /new, /insert, /delete, /edit, /update, /list
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/new", "/insert", "/delete", "/edit", "/update", "/list"})
public class ProductServlet extends HttpServlet {

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

        List<Product> products = productDAO.selectAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/views/product-list.jsp").forward(request, response);
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
            throws IOException {

        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        String urlImage = request.getParameter("urlImage");

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
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        String urlImage = request.getParameter("urlImage");

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
}
