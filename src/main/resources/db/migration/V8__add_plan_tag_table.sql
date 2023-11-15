CREATE TABLE IF NOT EXISTS plan_tag
(
    plan_tag_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id     BIGINT       NOT NULL,
    tag_id      BIGINT       NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL
);