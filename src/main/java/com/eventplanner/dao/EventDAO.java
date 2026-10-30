package com.eventplanner.dao;

import com.eventplanner.model.Event;
import com.eventplanner.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Database access methods for the events table.
 */
public class EventDAO {

    private static final String SELECT_WITH_JOINS =
            "SELECT e.*, c.category_name, u.full_name AS organizer_name "
            + "FROM events e "
            + "JOIN categories c ON e.category_id = c.category_id "
            + "LEFT JOIN users u ON e.user_id = u.user_id ";

    public int create(Event event) throws SQLException {
        String sql = "INSERT INTO events (user_id, category_id, event_title, description, event_date, "
                + "start_time, end_time, location, contact_information, event_image) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (event.getUserId() == null) {
                statement.setNull(1, java.sql.Types.INTEGER);
            } else {
                statement.setInt(1, event.getUserId());
            }
            statement.setInt(2, event.getCategoryId());
            statement.setString(3, event.getEventTitle());
            statement.setString(4, event.getDescription());
            statement.setDate(5, event.getEventDate());
            statement.setTime(6, event.getStartTime());
            statement.setTime(7, event.getEndTime());
            statement.setString(8, event.getLocation());
            statement.setString(9, event.getContactInformation());
            statement.setString(10, event.getEventImage());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean update(Event event) throws SQLException {
        String sql = "UPDATE events SET category_id = ?, event_title = ?, description = ?, event_date = ?, "
                + "start_time = ?, end_time = ?, location = ?, contact_information = ?, event_image = ? "
                + "WHERE event_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, event.getCategoryId());
            statement.setString(2, event.getEventTitle());
            statement.setString(3, event.getDescription());
            statement.setDate(4, event.getEventDate());
            statement.setTime(5, event.getStartTime());
            statement.setTime(6, event.getEndTime());
            statement.setString(7, event.getLocation());
            statement.setString(8, event.getContactInformation());
            statement.setString(9, event.getEventImage());
            statement.setInt(10, event.getEventId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int eventId) throws SQLException {
        String sql = "DELETE FROM events WHERE event_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventId);
            return statement.executeUpdate() > 0;
        }
    }

    public Event findById(int eventId) throws SQLException {
        String sql = SELECT_WITH_JOINS + "WHERE e.event_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventId);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapRow(result);
                }
            }
        }
        return null;
    }

    /**
     * Searches events by title/description/location, category and time
     * period (upcoming, past, all).
     */
    public List<Event> search(String keyword, Integer categoryId, String timeframe) throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT_WITH_JOINS).append("WHERE 1 = 1 ");
        List<Object> parameters = new ArrayList<>();

        if (!isBlank(keyword)) {
            sql.append("AND (e.event_title LIKE ? OR e.description LIKE ? OR e.location LIKE ?) ");
            String pattern = "%" + keyword.trim() + "%";
            parameters.add(pattern);
            parameters.add(pattern);
            parameters.add(pattern);
        }

        if (categoryId != null && categoryId > 0) {
            sql.append("AND e.category_id = ? ");
            parameters.add(categoryId);
        }

        if ("upcoming".equals(timeframe)) {
            sql.append("AND e.event_date >= CURDATE() ");
            sql.append("ORDER BY e.event_date ASC, e.start_time ASC");
        } else if ("past".equals(timeframe)) {
            sql.append("AND e.event_date < CURDATE() ");
            sql.append("ORDER BY e.event_date DESC, e.start_time DESC");
        } else {
            sql.append("ORDER BY e.event_date DESC, e.start_time DESC");
        }

        List<Event> events = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            bindParameters(statement, parameters);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    events.add(mapRow(result));
                }
            }
        }
        return events;
    }

    public List<Event> findUpcoming(int limit) throws SQLException {
        String sql = SELECT_WITH_JOINS
                + "WHERE e.event_date >= CURDATE() "
                + "ORDER BY e.event_date ASC, e.start_time ASC LIMIT ?";
        List<Event> events = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    events.add(mapRow(result));
                }
            }
        }
        return events;
    }

    public List<Event> findRecent(int limit) throws SQLException {
        String sql = SELECT_WITH_JOINS + "ORDER BY e.created_at DESC LIMIT ?";
        List<Event> events = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    events.add(mapRow(result));
                }
            }
        }
        return events;
    }

    public List<Event> findByUser(int userId) throws SQLException {
        String sql = SELECT_WITH_JOINS + "WHERE e.user_id = ? ORDER BY e.event_date DESC, e.start_time DESC";
        List<Event> events = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    events.add(mapRow(result));
                }
            }
        }
        return events;
    }

    public List<Event> findRecentByUser(int userId, int limit) throws SQLException {
        String sql = SELECT_WITH_JOINS + "WHERE e.user_id = ? ORDER BY e.created_at DESC LIMIT ?";
        List<Event> events = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, limit);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    events.add(mapRow(result));
                }
            }
        }
        return events;
    }

    public int countAll() throws SQLException {
        return count("SELECT COUNT(*) FROM events");
    }

    public int countUpcoming() throws SQLException {
        return count("SELECT COUNT(*) FROM events WHERE event_date >= CURDATE()");
    }

    public int countByUser(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM events WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? result.getInt(1) : 0;
            }
        }
    }

    private int count(String sql) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            return result.next() ? result.getInt(1) : 0;
        }
    }

    private void bindParameters(PreparedStatement statement, List<Object> parameters) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            Object value = parameters.get(i);
            if (value instanceof Integer) {
                statement.setInt(i + 1, (Integer) value);
            } else {
                statement.setString(i + 1, value.toString());
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private Event mapRow(ResultSet result) throws SQLException {
        Event event = new Event();
        event.setEventId(result.getInt("event_id"));

        int userId = result.getInt("user_id");
        if (result.wasNull()) {
            event.setUserId(null);
        } else {
            event.setUserId(userId);
        }

        event.setCategoryId(result.getInt("category_id"));
        event.setEventTitle(result.getString("event_title"));
        event.setDescription(result.getString("description"));
        event.setEventDate(result.getDate("event_date"));
        event.setStartTime(result.getTime("start_time"));
        event.setEndTime(result.getTime("end_time"));
        event.setLocation(result.getString("location"));
        event.setContactInformation(result.getString("contact_information"));
        event.setEventImage(result.getString("event_image"));
        event.setCreatedAt(result.getTimestamp("created_at"));
        event.setCategoryName(result.getString("category_name"));
        event.setOrganizerName(result.getString("organizer_name"));
        return event;
    }
}
