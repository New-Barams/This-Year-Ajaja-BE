CREATE TABLE IF NOT EXISTS footprint_ajajas
(
    footprint_id BIGINT NOT NULL,
    id           bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS footprint_tags
(
    footprint_id BIGINT      NOT NULL,
    id           bigint      NOT NULL,
    name         VARCHAR(10) NOT NULL,
    PRIMARY KEY (footprint_id, id, name)
);

CREATE TABLE IF NOT EXISTS footprints
(
    footprint_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    target_id       BIGINT            NOT NULL,
    target_title    VARCHAR(50)       NOT NULL,
    writer_id       BIGINT            NOT NULL,
    nickname        VARCHAR(20)       NOT NULL,
    title           VARCHAR(50)       NOT NULL,
    footprint_type  VARCHAR(10)       NOT NULL,
    visible         BIT               NOT NULL,
    deleted         BIT DEFAULT FALSE NOT NULL,
    content         TEXT,
    keep_content    TEXT,
    problem_content TEXT,
    try_content     TEXT,
    created_at      TIMESTAMP(6)      NOT NULL,
    updated_at      TIMESTAMP(6)      NOT NULL
);

ALTER TABLE footprint_ajajas
    ADD CONSTRAINT FK_footprint_ajajas
        FOREIGN KEY (footprint_id)
            REFERENCES footprints (footprint_id);

ALTER TABLE footprint_tags
    ADD CONSTRAINT FK_footprint_tags
        FOREIGN KEY (footprint_id)
            REFERENCES footprints (footprint_id);
