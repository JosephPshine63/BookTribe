-- Demo data, loaded by DataSeeder when SEED_DATA=true and the feedback table is empty.
-- book_id refers to the fixed ids of book-service's seed/books.sql; created_by is the Keycloak
-- user id of the reviewer, who borrowed that book in the seeded history.

INSERT INTO feedback (id, note, comment, book_id, created_date, created_by) VALUES
(1, 5.0, 'A perfect adventure, I read it in two evenings.', 1,  now() - interval '35 days', '263fd6df-e73a-459b-9c7d-b8385fe92667'),
(2, 4.0, 'Still unsettling decades later.', 2,  now() - interval '14 days', '53ec11b3-519e-4ce3-8539-f5a70b2893f2'),
(3, 4.5, 'Moving and beautifully written.', 4,  now() - interval '26 days', '00d91309-439b-4963-a172-6981c85bfe88'),
(4, 3.5, 'Great atmosphere, a bit slow in the middle.', 5,  now() - interval '18 days', 'a0ebe115-9ae9-467e-b2eb-29eecd15168b'),
(5, 4.0, 'Lots of practical tips, some chapters feel dated.', 8,  now() - interval '22 days', '263fd6df-e73a-459b-9c7d-b8385fe92667'),
(6, 3.0, 'Interesting ideas, but the characters are flat.', 10, now() - interval '10 days', '2fe4e2d9-6dda-42a9-871f-60d169ba5795');

-- Move the Hibernate sequence past the fixed ids so that new rows do not collide with them.
SELECT setval('feedback_seq', GREATEST(1000, (SELECT last_value FROM feedback_seq)));
