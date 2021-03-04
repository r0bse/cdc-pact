-- liquibase formatted SQL

-- changeset mail.me@trash-mail.com:2021-03-03_20.00
--
CREATE TABLE superhero
(
    id BIGINT not null,
    version INT not null,
    created_at timestamp not null,
    last_modified timestamp not null,
    name varchar NOT NULL unique,
    identity varchar NOT NULL unique,
    affiliation varchar NOT NULL,
    PRIMARY KEY (id)
);
insert into superhero(id, version, created_at, last_modified, name, identity, affiliation) values
(1, 0, now(), now(), 'Batman', 'Bruce Wayne', 'DC'),
(2, 0, now(), now(), 'Spider-Man', 'Peter Parker', 'Marvel'),
(3, 0, now(), now(), 'Spider-Gwen', 'Gwen Stacy', 'Marvel'),
(4, 0, now(), now(), 'Spider-Ham', 'Peter Porker', 'Marvel'),
(5, 0, now(), now(), 'Superman', 'Clark Kent', 'DC');