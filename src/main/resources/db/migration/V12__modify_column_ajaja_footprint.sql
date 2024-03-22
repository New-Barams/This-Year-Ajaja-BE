ALTER TABLE footprints
DROP COLUMN keep_content,
DROP COLUMN problem_content,
DROP COLUMN try_content,
ADD COLUMN emotion TEXT,
ADD COLUMN reason TEXT,
ADD COLUMN strengths TEXT,
ADD COLUMN weaknesses TEXT,
ADD COLUMN Jujuljujul TEXT,
ADD COLUMN icon_number  INTEGER  NOT NULL AFTER footprint_type;
