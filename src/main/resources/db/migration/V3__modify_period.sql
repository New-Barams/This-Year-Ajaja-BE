use ajaja;
ALTER TABLE reminds
    DROP COLUMN starts,
    DROP COLUMN ends,
    ADD COLUMN remind_month int not null,
    ADD COLUMN remind_day   int not null;
ALTER TABLE remind_messages
    DROP COLUMN message_idx,
    ADD COLUMN remind_month int not null,
    ADD COLUMN remind_day   int not null;

