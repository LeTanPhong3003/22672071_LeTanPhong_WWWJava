package dao;

import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Product entity
 * Handles all database operations for products table
 */
public class ProductDAO {

    /**
     * Get all products from database
     * @return List of all products
     */
    public List<Product> selectAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String urlImage = rs.getString("url_image");

                Product product = new Product(id, name, price, urlImage);
                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println("Error selecting all products: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }

    /**
     * Get a single product by ID
     * @param id Product ID
     * @return Product object or null if not found
     */
    public Product selectProductById(int id) {
        Product product = null;
        String sql = "SELECT * FROM products WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String urlImage = rs.getString("url_image");

                product = new Product(id, name, price, urlImage);
            }

            rs.close();

        } catch (SQLException e) {
            System.err.println("Error selecting product by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return product;
    }

    /**
     * Insert a new product into database
     * @param product Product object to insert
     * @return true if insert successful, false otherwise
     */
    public boolean insertProduct(Product product) {
        String sql = "INSERT INTO products (name, price, url_image) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getUrlImage());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Get the generated ID and set it to the product object
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error inserting product: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update an existing product in database
     * @param product Product object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, url_image = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setString(3, product.getUrlImage());
            pstmt.setInt(4, product.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Delete a product from database by ID
     * @param id Product ID to delete
     * @return true if delete successful, false otherwise
     */
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Count total number of products in database
     * @return Total number of products
     */
    public int countProducts() {
        String sql = "SELECT COUNT(*) as total FROM products";
        int count = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error counting products: " + e.getMessage());
            e.printStackTrace();
        }

        return count;
    }
}
