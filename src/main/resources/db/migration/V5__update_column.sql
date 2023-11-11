ALTER TABLE tags
    DROP COLUMN plan_id,
    ADD COLUMN plan_id BIGINT NULL;

ALTER TABLE plans
    CHANGE COLUMN is_remindable can_remind BOOLEAN NOT NULL,
    ADD COLUMN can_ajaja BOOLEAN NOT NULL;
