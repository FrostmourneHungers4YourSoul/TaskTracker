--liquibase formatted sql

--changeset admin:create-users
CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL,
    username   VARCHAR(50)  UNIQUE NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    first_name VARCHAR(50),
    last_name  VARCHAR(50),
    role       VARCHAR(20)         NOT NULL DEFAULT ('USER'),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email UNIQUE (email)
);