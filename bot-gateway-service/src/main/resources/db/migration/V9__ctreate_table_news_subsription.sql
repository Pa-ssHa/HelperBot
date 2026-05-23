CREATE TABLE IF NOT EXISTS helperbot.news_subscription (
                                                           chat_id BIGINT PRIMARY KEY,
                                                           theme VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
    );