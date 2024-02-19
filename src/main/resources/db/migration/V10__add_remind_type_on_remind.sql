ALTER TABLE reminds
    ADD COLUMN end_point varchar(10) not null after remind_type;
