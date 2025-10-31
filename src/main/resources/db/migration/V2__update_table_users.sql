SET search_path to helperbot;

ALTER TABLE users
    ADD COLUMN city VARCHAR(100),
    ADD COLUMN age INTEGER;

ALTER TABLE users
    DROP COLUMN IF EXISTS desired_salary;

ALTER TABLE users
    RENAME COLUMN telegram_id TO chat_id


