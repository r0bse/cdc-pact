-- liquibase formatted SQL

-- changeset mail.me@trash-mail.com:2021-03-03_20.00
--
-- create sequence public.hibernate_sequence;
CREATE TABLE superhero
(
    id SERIAL,
    version INT not null,
    created_at timestamp not null,
    last_modified timestamp not null,
    name varchar NOT NULL unique,
    identity varchar NOT NULL unique,
    affiliation varchar NOT NULL,
    PRIMARY KEY (id)
);
insert into superhero(version, created_at, last_modified, name, identity, affiliation) values
(0, now(), now(), 'Batman', 'Bruce Wayne', 'DC'),
(0, now(), now(), 'Spider-Man', 'Peter Parker', 'Marvel'),
(0, now(), now(), 'Spider-Gwen', 'Gwen Stacy', 'Marvel'),
(0, now(), now(), 'Spider-Ham', 'Peter Porker', 'Marvel'),
(0, now(), now(), 'Superman', 'Clark Kent', 'DC');