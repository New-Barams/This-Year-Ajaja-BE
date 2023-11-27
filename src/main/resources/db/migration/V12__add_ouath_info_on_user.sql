ALTER TABLE users
    ADD COLUMN oauth_id       BIGINT      NOT NULL,
    ADD COLUMN oauth_provider VARCHAR(20) NOT NULL
