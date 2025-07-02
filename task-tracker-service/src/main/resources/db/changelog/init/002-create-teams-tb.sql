--liquibase formatted sql

--changeset admin:create-teams
CREATE TABLE IF NOT EXISTS teams
(
    id          BIGSERIAL,
    name        VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_by  BIGINT              NOT NULL,
    created_at  TIMESTAMP DEFAULT now(),
    updated_at  TIMESTAMP DEFAULT now(),
    CONSTRAINT pk_teams PRIMARY KEY (id),
    CONSTRAINT fk_teams_created_by FOREIGN KEY (created_by)
        REFERENCES users (id) ON DELETE SET NULL
);