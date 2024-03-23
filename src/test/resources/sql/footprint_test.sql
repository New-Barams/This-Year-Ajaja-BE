-- 유저 데이터 생성
INSERT INTO users (nickname, phone_number, email, remind_email, verified, remind_type, deleted)
VALUES ('User1', '1234567890', 'user1@example.com', 'reminder1@example.com', true, 'EMAIL', false),
       ('User2', '1234567890', 'user2@example.com', 'reminder2@example.com', true, 'EMAIL', false),
       ('User3', '1234567890', 'user3@example.com', 'reminder3@example.com', true, 'EMAIL', false),
       ('User4', '1234567890', 'user3@example.com', 'reminder3@example.com', true, 'EMAIL', false),
       ('User5', '1234567890', 'user3@example.com', 'reminder3@example.com', true, 'EMAIL', false);

INSERT INTO plans (user_id, icon_number, title, description, remind_total_period, remind_term, remind_date, remind_time,
                   is_public, can_remind, can_ajaja, deleted)
VALUES (1, 1, 'Title 1', 'Description 1', 30, 7, 1, CURRENT_TIMESTAMP, true, true, true, false),
       (2, 2, 'Title 2', 'Description 2', 30, 7, 1, CURRENT_TIMESTAMP, true, true, true, false),
       (3, 3, 'Title 3', 'Description 3', 30, 7, 1, CURRENT_TIMESTAMP, true, true, true, false),
       (4, 4, 'Title 4', 'Description 4', 30, 7, 1, CURRENT_TIMESTAMP, true, true, true, false),
       (5, 5, 'Title 5', 'Description 5', 30, 7, 1, CURRENT_TIMESTAMP, true, true, true, false);

INSERT INTO footprints (target_id, writer_id, footprint_type, icon_number, title, visible, deleted, content, emotion,
                        reason, strengths, weaknesses, jujuljujul)
VALUES (1, 1, 'FREE', 1, 'Title 1', true, false, 'Content 1', 'Emotion 1', 'Reason 1', 'Strengths 1', 'Weaknesses 1',
        'Jujuljujul 1'),
       (1, 1, 'FREE', 1, 'Title 2', true, false, 'Content 2', 'Emotion 2', 'Reason 2', 'Strengths 2', 'Weaknesses 2',
        'Jujuljujul 2'),
       (2, 2, 'FREE', 1, 'Title 3', true, false, 'Content 3', 'Emotion 3', 'Reason 3', 'Strengths 3', 'Weaknesses 3',
        'Jujuljujul 3'),
       (2, 2, 'FREE', 1, 'Title 4', true, false, 'Content 4', 'Emotion 4', 'Reason 4', 'Strengths 4', 'Weaknesses 4',
        'Jujuljujul 4'),
       (3, 3, 'FREE', 1, 'Title 5', true, false, 'Content 5', 'Emotion 5', 'Reason 5', 'Strengths 5', 'Weaknesses 5',
        'Jujuljujul 5'),
       (3, 3, 'AJAJA', 1, 'Title 6', true, false, 'Content 6', 'Emotion 6', 'Reason 6', 'Strengths 6', 'Weaknesses 6',
        'Jujuljujul 6'),
       (4, 4, 'AJAJA', 1, 'Title 7', true, false, 'Content 7', 'Emotion 7', 'Reason 7', 'Strengths 7', 'Weaknesses 7',
        'Jujuljujul 7'),
       (4, 4, 'AJAJA', 1, 'Title 8', true, false, 'Content 8', 'Emotion 8', 'Reason 8', 'Strengths 8', 'Weaknesses 8',
        'Jujuljujul 8'),
       (5, 5, 'AJAJA', 1, 'Title 9', true, false, 'Content 9', 'Emotion 9', 'Reason 9', 'Strengths 9', 'Weaknesses 9',
        'Jujuljujul 9'),
       (5, 5, 'AJAJA', 1, 'Title 10', true, false, 'Content 10', 'Emotion 10', 'Reason 10', 'Strengths 10',
        'Weaknesses 10', 'Jujuljujul 10');

INSERT INTO tags(tag_name)
VALUES ('Tag1'),
       ('Tag2'),
       ('Tag3');

INSERT INTO footprint_tags(footprint_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 1),
       (3, 2),
       (3, 3),
       (4, 1),
       (4, 2),
       (4, 3),
       (5, 1),
       (5, 2),
       (5, 3);

INSERT INTO ajajas (target_id, user_id, is_canceled, target_type, created_at, updated_at)
VALUES
    (1, 1, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 1, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 1, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 2, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 2, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 2, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 3, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 3, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 3, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 4, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 4, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 4, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 5, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 5, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 5, false, 'FOOTPRINT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
