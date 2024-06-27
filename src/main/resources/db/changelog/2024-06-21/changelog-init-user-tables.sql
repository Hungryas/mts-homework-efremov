--liquibase formatted sql

--changeset liquibase-user:4 splitStatements:true endDelimiter:;

create table if not exists public.user_roles
(
    name       character varying(50) primary key not null, -- Наименование
    is_enabled boolean                           not null  -- Статус
);
comment on column public.user_roles.name is 'Наименование';
comment on column public.user_roles.is_active is 'Статус';

--rollback drop table public.user_roles;

--changeset liquibase-user:5 splitStatements:true endDelimiter:;

create table if not exists public.users
(
    email      character varying(250) primary key not null, -- Электронная почта
    password   character varying(250)             not null, -- Пароль
    role       character varying(50)              not null, -- Роль
    is_enabled boolean                            not null, -- Статус
    foreign key (role) references public.user_roles (name)
        match simple on update cascade on delete restrict
);
comment on column public.users.email is 'Электронная почта';
comment on column public.users.password is 'Пароль';
comment on column public.users.role is 'Роль';
comment on column public.users.is_enabled is 'Статус';

--rollback drop table public.users;