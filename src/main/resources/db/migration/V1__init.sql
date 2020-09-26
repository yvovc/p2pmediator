CREATE SCHEMA IF NOT EXISTS p2pmediatordb;

CREATE TABLE p2pmediatordb.peer (
    id UUID PRIMARY KEY,
    username VARCHAR,
    password VARCHAR
);

CREATE TABLE p2pmediatordb.video_game (
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    description VARCHAR
);

CREATE TABLE p2pmediatordb.streaming_session_status (
    id SERIAL PRIMARY KEY,
    value VARCHAR
);

CREATE TABLE p2pmediatordb.view_request_status (
    id SERIAL PRIMARY KEY,
    value VARCHAR
);

CREATE TABLE p2pmediatordb.streaming_session (
    id SERIAL PRIMARY KEY,
    host_id UUID REFERENCES p2pmediatordb.peer(id) ON DELETE CASCADE,
    video_game_id INTEGER REFERENCES p2pmediatordb.video_game(id) ON DELETE CASCADE,
    streaming_session_status INTEGER REFERENCES p2pmediatordb.streaming_session_status(id) ON DELETE CASCADE
);

CREATE TABLE p2pmediatordb.view_request (
    id SERIAL PRIMARY KEY,
    viewer_id UUID REFERENCES p2pmediatordb.peer(id) ON DELETE CASCADE,
    streaming_session_id INTEGER REFERENCES p2pmediatordb.streaming_session(id) ON DELETE CASCADE,
    view_request_status INTEGER REFERENCES p2pmediatordb.view_request_status(id) ON DELETE CASCADE
);

CREATE TABLE p2pmediatordb.streaming_session_endpoint (
    id SERIAL PRIMARY KEY,
    streaming_session_id INTEGER REFERENCES p2pmediatordb.streaming_session(id) ON DELETE CASCADE,
    host VARCHAR,
    port INTEGER
);

CREATE TABLE p2pmediatordb.view_request_endpoint (
    id SERIAL PRIMARY KEY,
    view_request_id INTEGER REFERENCES p2pmediatordb.view_request(id) ON DELETE CASCADE,
    host VARCHAR,
    port INTEGER
);