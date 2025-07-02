--liquibase formatted sql

--changeset admin:create-team_members
CREATE TABLE IF NOT EXISTS team_members
(
    id        BIGSERIAL,
    team_id   BIGINT NOT NULL,
    user_id   BIGINT NOT NULL,
    joined_at TIMESTAMP DEFAULT now(),
    CONSTRAINT pk_team_members PRIMARY KEY (id),
    CONSTRAINT fk_team_members_team FOREIGN KEY (team_id)
        REFERENCES teams (id) ON DELETE CASCADE,
    CONSTRAINT fk_team_members_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uq_team_members UNIQUE (team_id, user_id)
);