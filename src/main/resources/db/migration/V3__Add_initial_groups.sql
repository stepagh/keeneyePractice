INSERT INTO groups (id, name)
VALUES (nextval('groups_seq'), 'ИВТ-41'),
       (nextval('groups_seq'), 'ИВТ-42'),
       (nextval('groups_seq'), 'ИФСТ-22')
    ON CONFLICT (name) DO NOTHING;