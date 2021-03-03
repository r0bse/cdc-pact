
CREATE TABLE superhero
(
    id bigserial not null primary key,
    version bigserial not null,
    created_date timestamp not null,
    last_modified_date timestamp not null,
    name varchar NOT NULL unique,
    identity varchar NOT NULL unique,
    affiliation varchar NOT NULL
);