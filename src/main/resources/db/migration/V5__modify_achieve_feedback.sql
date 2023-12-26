ALTER TABLE feedbacks
    ADD COLUMN feedback_message varchar(100) not null,
    MODIFY achieve int not null;
ALTER TABLE plans
    DROP COLUMN achieve_rate;
