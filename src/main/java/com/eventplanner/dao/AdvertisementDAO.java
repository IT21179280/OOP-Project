package com.eventplanner.dao;

import com.eventplanner.model.Advertisement;
import com.eventplanner.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Database access methods for the advertisements table.
 */
public class AdvertisementDAO {

    private static final String SELECT_WITH_JOIN =
            "SELECT a.*, u.full_name AS organizer_name "
            + "FROM advertisements a "
            + "JOIN users u ON a.user_id = u.user_id ";

    public int create(Advertisement advertisement) throws SQLException {
        String sql = "INSERT INTO advertisements (user_id, event_id, title, description, event_name, "
                + "event_date, location, contact_number, email, image) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, advertisement.getUserId());
            if (advertisement.getEventId() == null) {
                statement.setNull(2, Types.INTEGER);
            } else {
                statement.setInt(2, advertisement.getEventId());
            }
            statement.setString(3, advertisement.getTitle());
            statement.setString(4, advertisement.getDescription());
            statement.setString(5, advertisement.getEventName());
            statement.setDate(6, advertisement.getEventDate());
            statement.setString(7, advertisement.getLocation());
            statement.setString(8, advertisement.getContactNumber());
            statement.setString(9, advertisement.getEmail());
            statement.setString(10, advertisement.getImage());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean update(Advertisement advertisement) throws SQLException {
        String sql = "UPDATE advertisements SET event_id = ?, title = ?, description = ?, event_name = ?, "
                + "event_date = ?, location = ?, contact_number = ?, email = ?, image = ? "
                + "WHERE advertisement_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (advertisement.getEventId() == null) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setInt(1, advertisement.getEventId());
            }
            statement.setString(2, advertisement.getTitle());
            statement.setString(3, advertisement.getDescription());
            statement.setString(4, advertisement.getEventName());
            statement.setDate(5, advertisement.getEventDate());
            statement.setString(6, advertisement.getLocation());
            statement.setString(7, advertisement.getContactNumber());
            statement.setString(8, advertisement.getEmail());
            statement.setString(9, advertisement.getImage());
            statement.setInt(10, advertisement.getAdvertisementId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int advertisementId) throws SQLException {
        String sql = "DELETE FROM advertisements WHERE advertisement_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, advertisementId);
            return statement.executeUpdate() > 0;
        }
    }

    public Advertisement findById(int advertisementId) throws SQLException {
        String sql = SELECT_WITH_JOIN + "WHERE a.advertisement_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, advertisementId);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapRow(result);
                }
            }
        }
        return null;
    }

    public List<Advertisement> findAll() throws SQLException {
        String sql = SELECT_WITH_JOIN + "ORDER BY a.created_at DESC";
        return readList(sql, null);
    }

    public List<Advertisement> search(String keyword) throws SQLException {
        String sql = SELECT_WITH_JOIN
                + "WHERE a.title LIKE ? OR a.event_name LIKE ? OR a.location LIKE ? "
                + "ORDER BY a.created_at DESC";
        List<Advertisement> advertisements = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String pattern = "%" + keyword.trim() + "%";
            statement.setString(1, pattern);
            statement.setString(2, pattern);
            statement.setString(3, pattern);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    advertisements.add(mapRow(result));
                }
            }
        }
        return advertisements;
    }

    public List<Advertisement> findRecent(int limit) throws SQLException {
        String sql = SELECT_WITH_JOIN + "ORDER BY a.created_at DESC LIMIT ?";
        List<Advertisement> advertisements = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    advertisements.add(mapRow(result));
                }
            }
        }
        return advertisements;
    }

    public List<Advertisement> findByUser(int userId) throws SQLException {
        String sql = SELECT_WITH_JOIN + "WHERE a.user_id = ? ORDER BY a.created_at DESC";
        List<Advertisement> advertisements = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    advertisements.add(mapRow(result));
                }
            }
        }
        return advertisements;
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM advertisements";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            return result.next() ? result.getInt(1) : 0;
        }
    }

    public int countByUser(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM advertisements WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getInt(1) : 0;
            }
        }
    }

    private List<Advertisement> readList(String sql, Integer parameter) throws SQLException {
        List<Advertisement> advertisements = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (parameter != null) {
                statement.setInt(1, parameter);
            }
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    advertisements.add(mapRow(result));
                }
            }
        }
        return advertisements;
    }

    private Advertisement mapRow(ResultSet result) throws SQLException {
        Advertisement advertisement = new Advertisement();
        advertisement.setAdvertisementId(result.getInt("advertisement_id"));
        advertisement.setUserId(result.getInt("user_id"));

        int eventId = result.getInt("event_id");
        if (result.wasNull()) {
            advertisement.setEventId(null);
        } else {
            advertisement.setEventId(eventId);
        }

        advertisement.setTitle(result.getString("title"));
        advertisement.setDescription(result.getString("description"));
        advertisement.setEventName(result.getString("event_name"));
        advertisement.setEventDate(result.getDate("event_date"));
        advertisement.setLocation(result.getString("location"));
        advertisement.setContactNumber(result.getString("contact_number"));
        advertisement.setEmail(result.getString("email"));
        advertisement.setImage(result.getString("image"));
        advertisement.setCreatedAt(result.getTimestamp("created_at"));
        advertisement.setOrganizerName(result.getString("organizer_name"));
        return advertisement;
    }
}
