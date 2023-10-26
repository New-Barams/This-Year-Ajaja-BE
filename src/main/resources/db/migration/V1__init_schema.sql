CREATE TABLE IF NOT EXISTS users
(
    user_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname    VARCHAR(20)  NOT NULL,
    email       VARCHAR(50)  NOT NULL UNIQUE,
    is_verified BOOLEAN      NOT NULL,
    status      VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS plans
(
    plan_id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id             BIGINT       NOT NULL,
    title               VARCHAR(50)  NOT NULL,
    description         VARCHAR(300) NOT NULL,
    remind_total_period INTEGER      NOT NULL,
    remind_term         INTEGER      NOT NULL,
    remind_date         INTEGER      NOT NULL,
    is_public           BOOLEAN      NOT NULL,
    is_remindable       BOOLEAN      NOT NULL,
    status              VARCHAR(20)  NOT NULL,
    created_at          TIMESTAMP(6) NOT NULL,
    updated_at          TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS feedbacks
(
    feedback_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id      BIGINT       NOT NULL,
    plan_id      BIGINT       NOT NULL,
    archive_rate VARCHAR(20)  NOT NULL,
    deadline     TIMESTAMP(6) NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    created_at   TIMESTAMP(6) NOT NULL,
    updated_at   TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS remind_messages
(
    message_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id     BIGINT       NOT NULL,
    message_idx INT          NOT NULL,
    content     VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS tags
(
    tag_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id    BIGINT       NOT NULL,
    tag_name   VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS ajajas
(
    ajaja_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    target_id   BIGINT       NOT NULL,
    target_type VARCHAR(20)  NOT NULL,
    is_canceled BOOLEAN      NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS reminds
(
    remind_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT       NOT NULL,
    plan_id     BIGINT       NOT NULL,
    remind_type VARCHAR(20)  NOT NULL,
    status      VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL
);
