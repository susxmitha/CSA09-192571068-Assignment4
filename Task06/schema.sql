CREATE DATABASE IF NOT EXISTS campus_events;
USE campus_events;

CREATE TABLE IF NOT EXISTS event (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(120) NOT NULL UNIQUE,
    event_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS registration (
    registration_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(30) NOT NULL,
    student_name VARCHAR(120) NOT NULL,
    event_id INT NOT NULL,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_registration_event
        FOREIGN KEY (event_id) REFERENCES event(event_id),
    CONSTRAINT uq_student_event UNIQUE (student_id, event_id)
);

INSERT IGNORE INTO event (event_name, event_date) VALUES
('Tech Symposium', '2026-12-10'),
('Cultural Fest', '2026-12-15'),
('Robotics Workshop', '2026-12-20');
