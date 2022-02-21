CREATE DATABASE workshop2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS users(
    id INT AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE,
    username VARCHAR(255),
    password VARCHAR(60),
    PRIMARY KEY (id)
);

UPDATE users SET username