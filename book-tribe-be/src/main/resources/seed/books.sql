-- Demo data, loaded by DataSeeder when SEED_DATA=true and the book table is empty.
-- created_by / user_id are the Keycloak user ids (JWT "sub") of the realm export:
--   00d91309-439b-4963-a172-6981c85bfe88  marco@mail.com
--   263fd6df-e73a-459b-9c7d-b8385fe92667  john@mail.com
--   53ec11b3-519e-4ce3-8539-f5a70b2893f2  dev.pioruocco@test.com
--   a0ebe115-9ae9-467e-b2eb-29eecd15168b  newuser@yopmail.com
--   2fe4e2d9-6dda-42a9-871f-60d169ba5795  testest@yopmail.com
--   b1a7ce0d-1450-4356-9fff-14165d4ce461  usernew@yopmail.com
-- Book ids are fixed because feedback-service's seed/feedbacks.sql refers to them.

INSERT INTO book (id, title, author_name, isbn, synopsis, book_cover, archived, shareable, created_date, created_by) VALUES
(1,  'The Hobbit', 'J.R.R. Tolkien', '9780547928227', 'Bilbo Baggins is swept into a quest to reclaim the dwarven kingdom of Erebor from the dragon Smaug.', NULL, false, true,  now() - interval '60 days', '00d91309-439b-4963-a172-6981c85bfe88'),
(2,  '1984', 'George Orwell', '9780451524935', 'Winston Smith works for the Party rewriting history, until he starts to doubt Big Brother.', NULL, false, true,  now() - interval '58 days', '00d91309-439b-4963-a172-6981c85bfe88'),
(3,  'Pride and Prejudice', 'Jane Austen', '9780141439518', 'Elizabeth Bennet and Mr Darcy misjudge each other in Regency England.', NULL, false, false, now() - interval '55 days', '00d91309-439b-4963-a172-6981c85bfe88'),
(4,  'To Kill a Mockingbird', 'Harper Lee', '9780061120084', 'Scout Finch grows up in Alabama while her father defends a Black man accused of a crime.', NULL, false, true,  now() - interval '50 days', '263fd6df-e73a-459b-9c7d-b8385fe92667'),
(5,  'The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'Nick Carraway is drawn into the world of his mysterious, wealthy neighbour Jay Gatsby.', NULL, false, true,  now() - interval '48 days', '263fd6df-e73a-459b-9c7d-b8385fe92667'),
(6,  'Dune', 'Frank Herbert', '9780441172719', 'Paul Atreides follows his family to the desert planet Arrakis, the only source of the spice.', NULL, true,  true,  now() - interval '45 days', '263fd6df-e73a-459b-9c7d-b8385fe92667'),
(7,  'Clean Code', 'Robert C. Martin', '9780132350884', 'Principles and practices for writing readable, maintainable code.', NULL, false, true,  now() - interval '40 days', '53ec11b3-519e-4ce3-8539-f5a70b2893f2'),
(8,  'The Pragmatic Programmer', 'David Thomas, Andrew Hunt', '9780135957059', 'Practical advice on the craft of software development, 20th anniversary edition.', NULL, false, true,  now() - interval '38 days', '53ec11b3-519e-4ce3-8539-f5a70b2893f2'),
(9,  'Sapiens', 'Yuval Noah Harari', '9780062316097', 'A brief history of humankind, from the cognitive revolution to the present.', NULL, false, true,  now() - interval '35 days', '53ec11b3-519e-4ce3-8539-f5a70b2893f2'),
(10, 'Brave New World', 'Aldous Huxley', '9780060850524', 'A future society engineered for stability and happiness, at the cost of freedom.', NULL, false, true,  now() - interval '30 days', 'a0ebe115-9ae9-467e-b2eb-29eecd15168b'),
(11, 'Frankenstein', 'Mary Shelley', '9780141439471', 'Victor Frankenstein creates life and is haunted by what he has made.', NULL, false, true,  now() - interval '25 days', '2fe4e2d9-6dda-42a9-871f-60d169ba5795'),
(12, 'Moby-Dick', 'Herman Melville', '9780142437247', 'Captain Ahab hunts the white whale that took his leg.', NULL, false, true,  now() - interval '20 days', 'b1a7ce0d-1450-4356-9fff-14165d4ce461');

-- returned + return_approved = closed loan; returned only = waiting for the owner's approval; neither = still borrowed.
INSERT INTO book_transaction_history (id, user_id, book_id, returned, return_approved, created_date, created_by) VALUES
(1, '263fd6df-e73a-459b-9c7d-b8385fe92667', 1,  true,  true,  now() - interval '40 days', '263fd6df-e73a-459b-9c7d-b8385fe92667'),
(2, '53ec11b3-519e-4ce3-8539-f5a70b2893f2', 2,  true,  false, now() - interval '15 days', '53ec11b3-519e-4ce3-8539-f5a70b2893f2'),
(3, '00d91309-439b-4963-a172-6981c85bfe88', 7,  false, false, now() - interval '10 days', '00d91309-439b-4963-a172-6981c85bfe88'),
(4, '00d91309-439b-4963-a172-6981c85bfe88', 4,  true,  true,  now() - interval '30 days', '00d91309-439b-4963-a172-6981c85bfe88'),
(5, '263fd6df-e73a-459b-9c7d-b8385fe92667', 8,  true,  true,  now() - interval '25 days', '263fd6df-e73a-459b-9c7d-b8385fe92667'),
(6, '53ec11b3-519e-4ce3-8539-f5a70b2893f2', 1,  false, false, now() - interval '5 days',  '53ec11b3-519e-4ce3-8539-f5a70b2893f2'),
(7, 'a0ebe115-9ae9-467e-b2eb-29eecd15168b', 5,  true,  true,  now() - interval '20 days', 'a0ebe115-9ae9-467e-b2eb-29eecd15168b'),
(8, '2fe4e2d9-6dda-42a9-871f-60d169ba5795', 10, true,  true,  now() - interval '12 days', '2fe4e2d9-6dda-42a9-871f-60d169ba5795');

-- Move the Hibernate sequences past the fixed ids so that new rows do not collide with them.
SELECT setval('book_seq', GREATEST(1000, (SELECT last_value FROM book_seq)));
SELECT setval('book_transaction_history_seq', GREATEST(1000, (SELECT last_value FROM book_transaction_history_seq)));
