--liquibase formatted sql

--changeset liquibase-user:1 splitStatements:true endDelimiter:;

create table if not exists animals.animal_type
(
    name        character varying(50) primary key not null, -- Вид
    is_predator boolean                                     -- Принадлежность к плотоядным
);
comment on column animals.animal_type.name is 'Вид';
comment on column animals.animal_type.is_predator is 'Принадлежность к плотоядным';

--rollback drop table animal_type;

--changeset liquibase-user:2 splitStatements:true endDelimiter:;

create table if not exists animals.animals
(
    id                 uuid primary key      not null default gen_random_uuid(), -- Идентификатор
    type               character varying(50) not null,                           -- Вид
    breed              character varying(50),                                    -- Порода
    name               character varying(50) not null,                           -- Кличка
    birth_date         date,                                                     -- Дата рождения
    character          character varying(50),                                    -- Характер
    cost               integer                        default 0,                 -- Стоимость
    secret_information character varying(250),                                   -- Дополнительная информация
    foreign key (type) references animals.animal_type (name)
        match simple on update cascade on delete restrict
);
comment on column animals.animals.id is 'Идентификатор';
comment on column animals.animals.type is 'Вид';
comment on column animals.animals.breed is 'Порода';
comment on column animals.animals.name is 'Кличка';
comment on column animals.animals.birth_date is 'Дата рождения';
comment on column animals.animals.character is 'Характер';
comment on column animals.animals.cost is 'Стоимость';
comment on column animals.animals.secret_information is 'Дополнительная информация';

--rollback drop table animals;