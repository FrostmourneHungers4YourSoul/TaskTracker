--liquibase formatted sql

--changeset admin:create-refresh-tokens
CREATE TABLE IF NOT EXISTS refresh_tokens
(
    id          BIGSERIAL,
    user_id     BIGINT       NOT NULL,
    token       VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP    NOT NULL,
    is_revoked  BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id),
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE
);