SET SEARCH_PATH TO p2pmediatordb;

INSERT INTO streaming_session_status(value) VALUES
    ('new'),
    ('ready'),
    ('active'),
    ('finished');

INSERT INTO view_request_status(value) VALUES
    ('new'),
    ('ready'),
    ('completed');

    INSERT INTO peer_role(value) VALUES
    ('streamer'),
    ('viewer');

INSERT INTO video_game(name, description) VALUES
    ('Need For Speed MW', 'Some Arcade racing'),
    ('CS:GO', 'Cyberdisciplined Shooter');

INSERT INTO peer(id, username, password) VALUES
    ('67df8472-d30e-4cb9-bce3-e7fad5f93827', 'super_streamer', '$2a$10$FlbJeRNIWllRrUjVNR5ttO4ICHNixiswRcFdJbt2HiLy2EK1u/nEO'),
    ('49489489-f0b2-4254-87e1-4c8af47ee379', 'super_viewer','$2a$10$FlbJeRNIWllRrUjVNR5ttO4ICHNixiswRcFdJbt2HiLy2EK1u/nEO');