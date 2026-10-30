package com.eventplanner.dao;

import com.eventplanner.model.Category;
import com.eventplanner.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Database access methods for the categories table.
 */
public class CategoryDAO {

    public List<Category> findAll() throws SQLException {
        String sql = "SELECT c.category_id, c.category_name, COUNT(e.event_id) AS event_count "
                + "FROM categories c "
                + "LEFT JOIN events e ON c.category_id = e.category_id "
                + "GROUP BY c.category_id, c.category_name "
                + "ORDER BY c.category_name";
        List<Category> categories = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Category category = new Category();
                category.setCategoryId(result.getInt("category_id"));
                category.setCategoryName(result.getString("category_name"));
                category.setEventCount(result.getInt("event_count"));
                categories.add(category);
            }
        }
        return categories;
    }

    public Category findById(int categoryId) throws SQLException {
        String sql = "SELECT category_id, category_name FROM categories WHERE category_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    Category category = new Category();
                    category.setCategoryId(result.getInt("category_id"));
                    category.setCategoryName(result.getString("category_name"));
                    return category;
                }
            }
        }
        return null;
    }

    public boolean isNameTaken(String name, Integer excludeId) throws SQLException {
        String sql = excludeId == null
                ? "SELECT category_id FROM categories WHERE category_name = ?"
                : "SELECT category_id FROM categories WHERE category_name = ? AND category_id <> ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            if (excludeId != null) {
                statement.setInt(2, excludeId);
            }
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    public int create(String categoryName) throws SQLException {
        String sql = "INSERT INTO categories (category_name) VALUES (?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, categoryName);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean update(int categoryId, String categoryName) throws SQLException {
        String sql = "UPDATE categories SET category_name = ? WHERE category_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoryName);
            statement.setInt(2, categoryId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            return statement.executeUpdate() > 0;
        }
    }

    public int countEvents(int categoryId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM events WHERE category_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getInt(1) : 0;
            }
        }
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM categories";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            return result.next() ? result.getInt(1) : 0;
        }
    }
}
