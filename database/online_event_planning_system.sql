-- =====================================================================
-- Online Event Planning System
-- MySQL database script
-- 2nd Year Group Project (2022)
-- =====================================================================
-- How to use:
--   1. Open MySQL command line or MySQL Workbench
--   2. Run this whole script
--   3. Check that the database and tables were created
--
-- Default administrator account (created by this script)
--   Username : admin
--   Password : admin123
-- (Change the password after the first login in a real deployment.)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS online_event_planning_system
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE online_event_planning_system;

-- Drop existing tables before creating them again (safe re-run).
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS advertisements;
DROP TABLE IF EXISTS contact_messages;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS admins;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------------------------------------------------------------------
-- Table: admins
-- Stores administrator login accounts (separate from normal users).
-- ---------------------------------------------------------------------
CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Table: users
-- Registered users of the system.
-- Passwords are stored as salted SHA-256 hashes (salt:hash).
-- ---------------------------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'user',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Table: categories
-- Event categories used when creating events.
-- ---------------------------------------------------------------------
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Table: events
-- Events created by registered users or by an administrator.
-- user_id is NULL when an event was added by an administrator.
-- ---------------------------------------------------------------------
CREATE TABLE events (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    category_id INT NOT NULL,
    event_title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    event_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    location VARCHAR(150) NOT NULL,
    contact_information VARCHAR(100) NOT NULL,
    event_image VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_events_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_events_category
        FOREIGN KEY (category_id) REFERENCES categories(category_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Table: advertisements
-- Event advertisements published by registered users.
-- event_id links the advertisement to an event (optional).
-- event_name is stored so that the advertisement still shows the event
-- name if the related event is deleted by the owner or an administrator.
-- ---------------------------------------------------------------------
CREATE TABLE advertisements (
    advertisement_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    event_id INT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    event_name VARCHAR(150) NOT NULL,
    event_date DATE NOT NULL,
    location VARCHAR(150) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    image VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ads_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_ads_event
        FOREIGN KEY (event_id) REFERENCES events(event_id)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Table: contact_messages
-- Messages sent through the Contact Us page.
-- ---------------------------------------------------------------------
CREATE TABLE contact_messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    subject VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---------------------------------------------------------------------
-- Default data
-- ---------------------------------------------------------------------

-- Default administrator: admin / admin123
INSERT INTO admins (username, password) VALUES
('admin', 'a1b2c3d4e5f60718293a4b5c6d7e8f90:0212b518b03b50cc62a0dadc9e897f48190e21e0af3ba93665b4b0085885e265');

-- Event categories
INSERT INTO categories (category_name) VALUES
('Birthday Parties'),
('Weddings'),
('Music Events'),
('Educational Events'),
('Business Events'),
('Sports Events'),
('Social Events'),
('Other Events');

-- =====================================================================
-- End of script
-- =====================================================================
