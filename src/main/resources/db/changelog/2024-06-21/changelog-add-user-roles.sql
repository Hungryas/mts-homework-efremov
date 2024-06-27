--liquibase formatted sql

--changeset liquibase-user:6 splitStatements:true endDelimiter:;

INSERT INTO public.user_roles (name, is_active)
VALUES ('ADMIN', true),
       ('USER', true)

--rollback truncate public.user_roles;
