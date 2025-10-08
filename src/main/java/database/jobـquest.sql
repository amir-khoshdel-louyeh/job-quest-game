-- Create database
CREATE DATABASE IF NOT EXISTS job_quest;
USE job_quest;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    identity VARCHAR(50) NOT NULL
);

-- Items table
CREATE TABLE IF NOT EXISTS items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(30),
    owner_id INT,
    FOREIGN KEY (owner_id) REFERENCES users(id)
);
