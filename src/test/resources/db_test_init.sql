create schema if not exists animals;

create table if not exists animals.animal_type
(
    name        character varying(50) primary key not null, -- Вид
    is_predator boolean                                     -- Принадлежность к плотоядным
);

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