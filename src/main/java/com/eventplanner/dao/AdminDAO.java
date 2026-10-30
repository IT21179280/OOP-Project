package com.eventplanner.dao;

import com.eventplanner.model.Admin;
import com.eventplanner.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database access methods for the admins table.
 */
public class AdminDAO {

    public Admin findByUsername(String username) throws SQLException {
        String sql = "SELECT admin_id, username, password FROM admins WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    Admin admin = new Admin();
                    admin.setAdminId(result.getInt("admin_id"));
                    admin.setUsername(result.getString("username"));
                    admin.setPassword(result.getString("password"));
                    return admin;
                }
            }
        }
        return null;
    }
}
