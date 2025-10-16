CREATE SCHEMA IF NOT EXISTS helperbot;

SET search_path TO helperbot;

CREATE TABLE IF NOT EXISTS users(
    id SERIAL PRIMARY KEY,
    telegram_id BIGINT UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    profession VARCHAR(100),
    desired_salary INTEGER,
    created_at TIMESTAMP DEFAULT now()
);
