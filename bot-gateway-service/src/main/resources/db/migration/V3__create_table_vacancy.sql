SET search_path to helperbot;

CREATE TABLE vacancy(
    id_vacancy INTEGER PRIMARY KEY,
    name_vacancy VARCHAR(100),
    company VARCHAR(200),
    salary VARCHAR(60),
    city VARCHAR(30)
);

CREATE TABLE user_vacancy(
    user_id BIGINT NOT NULL REFERENCES helperbot.users(id) ON DELETE CASCADE,
    vacancy_id BIGINT NOT NULL REFERENCES helperbot.vacancy(id_vacancy) ON DELETE CASCADE,
    is_favorite BOOLEAN DEFAULT FALSE,
    is_hidden BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (user_id, vacancy_id)
);

