DROP TABLE ajajas;

CREATE TABLE ajajas
(
    target_id   BIGINT,
    user_id     BIGINT,
    target_type VARCHAR(20)  NOT NULL,
    is_canceled BOOLEAN      NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (target_id, user_id)
);
