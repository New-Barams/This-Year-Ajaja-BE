ALTER TABLE users
    ADD COLUMN phone_number varchar(11) not null,
    CHANGE COLUMN receive_type remind_type VARCHAR(20) NOT NULL;
