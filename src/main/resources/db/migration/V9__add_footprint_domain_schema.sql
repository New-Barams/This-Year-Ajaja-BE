CREATE TABLE IF NOT EXISTS footprints
(
    footprint_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    target_id       BIGINT       NOT NULL,
    writer_id       BIGINT       NOT NULL,
    title           VARCHAR(50)  NOT NULL,
    footprint_type  VARCHAR(10)  NOT NULL,
    visible         BOOLEAN      NOT NULL,
    deleted         BOOLEAN      NOT NULL,
    content         TEXT,
    keep_content    TEXT,
    problem_content TEXT,
    try_content     TEXT,
    created_at      TIMESTAMP(6) NOT NULL,
    updated_at      TIMESTAMP(6) NOT NULL
);
