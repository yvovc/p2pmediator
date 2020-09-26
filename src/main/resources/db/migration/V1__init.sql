CREATE SCHEMA IF NOT EXISTS p2pmediatordb;

CREATE TABLE p2pmediatordb.video_game (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    description VARCHAR
);
