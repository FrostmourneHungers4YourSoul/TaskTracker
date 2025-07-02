--liquibase formatted sql

--changeset admin:create-tasks
CREATE TABLE IF NOT EXISTS tasks
(
    id          BIGSERIAL,
    title       VARCHAR(100) NOT NULL,
    description TEXT,
    status      VARCHAR(20)  NOT NULL DEFAULT ('NEW'),
    priority    INTEGER      NOT NULL CHECK (priority BETWEEN 1 AND 5),
    category    VARCHAR(50),
    created_by  BIGINT       NOT NULL,
    assigned_to BIGINT,
    team_id     BIGINT,
    deadline    TIMESTAMP,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    CONSTRAINT pk_tasks PRIMARY KEY (id),
    CONSTRAINT fk_tasks_created_by FOREIGN KEY (created_by)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_assigned_to FOREIGN KEY (assigned_to)
        REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT fk_tasks_team FOREIGN KEY (team_id)
        REFERENCES teams (id) ON DELETE SET NULL
);