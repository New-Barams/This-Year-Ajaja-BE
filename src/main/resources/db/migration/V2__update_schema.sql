ALTER TABLE reminds
    ADD COLUMN content VARCHAR(255) NOT NULL,
    ADD COLUMN start   TIMESTAMP(6) NOT NULL,
    ADD COLUMN end     TIMESTAMP(6) NOT NULL,
    CHANGE COLUMN status is_deleted BOOLEAN NOT NULL;

ALTER TABLE tags
    ADD COLUMN updated_at TIMESTAMP(6) NOT NULL;

ALTER TABLE plans
    CHANGE COLUMN status is_deleted BOOLEAN NOT NULL;

ALTER TABLE remind_messages
    DROP COLUMN id,
    DROP COLUMN created_at,
    DROP COLUMN updated_at;

ALTER TABLE ajajas
    DROP COLUMN id;

ALTER TABLE feedbacks
    DROP COLUMN deadline,
    CHANGE COLUMN archive_rate achieve VARCHAR(20) NOT NULL,
    CHANGE COLUMN status is_deleted BOOLEAN NOT NULL;
