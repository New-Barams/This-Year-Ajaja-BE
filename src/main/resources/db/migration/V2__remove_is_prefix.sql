ALTER TABLE users
    CHANGE COLUMN is_verified verified BOOLEAN NOT NULL,
    CHANGE COLUMN is_deleted deleted BOOLEAN NOT NULL;

