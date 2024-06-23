--liquibase formatted sql

--changeset liquibase-user:3 splitStatements:true endDelimiter:;

INSERT INTO animals.animal_type (name, is_predator)
VALUES ('cat', true),
       ('dog', true),
       ('shark', true),
       ('wolf', true),
       ('cow', false),
       ('chicken', false),
       ('horse', false),
       ('sheep', false);

--rollback truncate animal_type;
