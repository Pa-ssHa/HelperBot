CREATE TABLE IF NOT EXISTS helperbot.news_article
(
    id VARCHAR(64) PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    url TEXT NOT NULL UNIQUE,
    source_name VARCHAR(255),
    theme VARCHAR(255),
    published_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS helperbot.user_news
(
    user_id BIGINT NOT NULL REFERENCES helperbot.users (chat_id) ON DELETE CASCADE,
    news_id VARCHAR(64) NOT NULL REFERENCES helperbot.news_article (id) ON DELETE CASCADE,
    is_viewed BOOLEAN DEFAULT TRUE,
    viewed_at TIMESTAMPTZ DEFAULT now(),
    PRIMARY KEY (user_id, news_id)
);
