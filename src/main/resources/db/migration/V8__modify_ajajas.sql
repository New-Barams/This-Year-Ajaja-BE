DROP TABLE ajajas;

CREATE TABLE IF NOT EXISTS ajajas
(
    ajaja_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    target_id   BIGINT       NULL,
    user_id     BIGINT       NOT NULL,
    target_type VARCHAR(20)  NOT NULL,
    is_canceled BOOLEAN      NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,
    UNIQUE KEY (target_id, user_id, target_type)
);
