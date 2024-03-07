CREATE TABLE IF NOT EXISTS footprint_tags (
   footprint_tag_id BIGINT PRIMARY KEY AUTO_INCREMENT,
   footprint_id     BIGINT       NOT NULL,
   tag_id           BIGINT       NOT NULL,
   created_at  TIMESTAMP(6) NOT NULL,
   updated_at  TIMESTAMP(6) NOT NULL
)
