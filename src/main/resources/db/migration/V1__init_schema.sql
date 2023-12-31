CREATE TABLE IF NOT EXISTS users
(
    user_id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname       VARCHAR(20)  NOT NULL,
    email          VARCHAR(50)  NOT NULL UNIQUE,
    remind_email   VARCHAR(50)  NOT NULL,
    is_verified    BOOLEAN      NOT NULL,
    oauth_id       BIGINT       NOT NULL,
    oauth_provider VARCHAR(20)  NOT NULL,
    receive_type   VARCHAR(20)  NOT NULL,
    is_deleted     BOOLEAN      NOT NULL,
    created_at     TIMESTAMP(6) NOT NULL,
    updated_at     TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS plans
(
    plan_id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id             BIGINT       NOT NULL,
    achieve_rate        INTEGER      NOT NULL,
    icon_number         INTEGER      NOT NULL,
    title               VARCHAR(50)  NOT NULL,
    description         VARCHAR(300) NOT NULL,
    remind_total_period INTEGER      NOT NULL,
    remind_term         INTEGER      NOT NULL,
    remind_date         INTEGER      NOT NULL,
    remind_time         VARCHAR(20)  NOT NULL,
    is_public           BOOLEAN      NOT NULL,
    can_remind          BOOLEAN      NOT NULL,
    can_ajaja           BOOLEAN      NOT NULL,
    is_deleted          BOOLEAN      NOT NULL,
    created_at          TIMESTAMP(6) NOT NULL,
    updated_at          TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS remind_messages
(
    plan_id     BIGINT       NOT NULL,
    message_idx INT          NOT NULL,
    content     VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS plan_tag
(
    plan_tag_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id     BIGINT       NOT NULL,
    tag_id      BIGINT       NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS tags
(
    tag_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    tag_name   VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS reminds
(
    remind_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT       NOT NULL,
    plan_id     BIGINT       NOT NULL,
    content     VARCHAR(255) NOT NULL,
    starts      TIMESTAMP(6) NOT NULL,
    ends        TIMESTAMP(6) NOT NULL,
    remind_type VARCHAR(20)  NOT NULL,
    is_deleted  BOOLEAN      NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS feedbacks
(
    feedback_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT       NOT NULL,
    plan_id     BIGINT       NOT NULL,
    achieve     VARCHAR(20)  NOT NULL,
    is_deleted  BOOLEAN      NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS ajajas
(
    ajaja_id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    target_id   BIGINT       NOT NULL,
    user_id     BIGINT       NOT NULL,
    target_type VARCHAR(20)  NOT NULL,
    is_canceled BOOLEAN      NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,
    UNIQUE KEY (target_id, user_id, target_type)
);
