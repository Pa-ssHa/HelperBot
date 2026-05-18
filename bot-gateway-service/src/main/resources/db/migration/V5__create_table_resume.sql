SET
search_path TO helperbot;

CREATE TABLE resume
(
    id               BIGSERIAL PRIMARY KEY,

    user_chat_id     BIGINT    NOT NULL,

    file_name        VARCHAR(255),
    telegram_file_id VARCHAR(255),

    score            INTEGER   NOT NULL,
    profession_match INTEGER   NOT NULL,

    strengths        TEXT,
    weaknesses       TEXT,
    recommendations  TEXT,

    raw_response     TEXT,

    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_resume_analysis_user
        FOREIGN KEY (user_chat_id)
            REFERENCES users (chat_id)
            ON DELETE CASCADE
);