-- liquibase formatted SQL

-- changeset mail.me@trash-mail.com:2022-05-09_20.00
--
alter table if exists superhero
    add column birthday timestamp;
UPDATE superhero SET birthday = '1970-01-01';