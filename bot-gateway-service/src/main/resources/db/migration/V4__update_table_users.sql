ALTER TABLE helperbot.user_vacancy
    DROP CONSTRAINT user_vacancy_user_id_fkey;

ALTER TABLE helperbot.users
    DROP CONSTRAINT users_pkey,
    DROP COLUMN id,
    ADD PRIMARY KEY (chat_id);

ALTER TABLE helperbot.user_vacancy
    ADD CONSTRAINT user_vacancy_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES helperbot.users (chat_id)
        ON DELETE CASCADE;
