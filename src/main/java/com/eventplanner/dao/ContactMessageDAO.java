package com.eventplanner.dao;

import com.eventplanner.model.ContactMessage;
import com.eventplanner.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Database access methods for the contact_messages table.
 */
public class ContactMessageDAO {

    public int create(ContactMessage message) throws SQLException {
        String sql = "INSERT INTO contact_messages (name, email, subject, message) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, message.getName());
            statement.setString(2, message.getEmail());
            statement.setString(3, message.getSubject());
            statement.setString(4, message.getMessage());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<ContactMessage> findAll() throws SQLException {
        String sql = "SELECT * FROM contact_messages ORDER BY created_at DESC";
        List<ContactMessage> messages = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                ContactMessage message = new ContactMessage();
                message.setMessageId(result.getInt("message_id"));
                message.setName(result.getString("name"));
                message.setEmail(result.getString("email"));
                message.setSubject(result.getString("subject"));
                message.setMessage(result.getString("message"));
                message.setCreatedAt(result.getTimestamp("created_at"));
                messages.add(message);
            }
        }
        return messages;
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM contact_messages";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            return result.next() ? result.getInt(1) : 0;
        }
    }
}
