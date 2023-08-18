INSERT INTO categories (name) VALUES('кино');

INSERT INTO categories (name) VALUES('танцы');

INSERT INTO categories (name) VALUES('театр');

INSERT INTO categories (name) VALUES('музыка');

INSERT INTO categories (name) VALUES('спорт');

INSERT INTO users (name, email) VALUES('Иван', '1email@eorig.com');

INSERT INTO users (name, email) VALUES('Николай', '2email@eorig.com');

INSERT INTO users (name, email) VALUES('Петр', '3email@eorig.com');

INSERT INTO users (name, email) VALUES('Вачик', '4email@eorig.com');

INSERT INTO users (name, email) VALUES('Саша', '5email@eorig.com');

INSERT INTO users (name, email) VALUES('Виктор', '6email@eorig.com');

INSERT INTO users (name, email) VALUES('Олег', '7email@eorig.com');

INSERT INTO users (name, email) VALUES('Сергей', '8email@eorig.com');

INSERT INTO users (name, email) VALUES('Иоган', '9email@eorig.com');


INSERT INTO events (annotation, category_id, created_on, description, event_date, initiator_id, lat, lon, paid, participant_limit, published_on, request_moderation, state, title)
VALUES('выступление барда', 4, '2022-08-17 15:00:00', 'первое событие', '2022-09-17 15:00:00', 1, 90, 80, 'false', 0, '2022-08-18 15:00:00', 'true', 'PUBLISHED', 'живая музыка');


INSERT INTO events
(annotation, category_id, created_on, description, event_date, initiator_id, lat, lon, paid, participant_limit, published_on, request_moderation, state, title)
VALUES('музыка группы КИНО', 4, '2023-01-17 15:00:00', 'второе событие', '2023-09-17 15:00:00', 1, 70, 90, 'false', 0, '2023-03-18 15:00:00', 'false', 'PUBLISHED', 'симфонический оркестр');

INSERT INTO events
(annotation, category_id, created_on, description, event_date, initiator_id, lat, lon, paid, participant_limit, published_on, request_moderation, state, title)
VALUES('перетягивание каната', 5, '2023-01-17 15:00:00', 'третие событие', '2023-08-10 15:00:00', 1, 60, 90, 'false', 0, '2023-03-20 15:00:00', 'false', 'PUBLISHED', 'командный спорт');

INSERT INTO events
(annotation, category_id, created_on, description, event_date, initiator_id, lat, lon, paid, participant_limit, published_on, request_moderation, state, title)
VALUES('Блэйд возвращается', 1, '2023-05-17 15:00:00', 'четвертое событие', '2023-08-17 15:00:00', 5, 60, 90, 'false', 0, '2023-05-20 15:00:00', 'false', 'PUBLISHED', 'новый фильм в кинотеатре');

INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2022-08-20 15:00:00', 1, 2, 'CONFIRMED');

INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2022-08-21 15:00:00', 1, 3, 'CONFIRMED');

INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2022-08-22 15:00:00', 1, 4, 'CONFIRMED');

INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2022-09-10 15:00:00', 1, 5, 'CONFIRMED');

INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2022-09-11 15:00:00', 2, 6, 'CONFIRMED');

INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2023-04-20 15:00:00', 3, 2, 'CONFIRMED');

INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2023-04-25 15:00:00', 3, 3, 'CONFIRMED');

INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2023-04-29 15:00:00', 3, 4, 'CONFIRMED');

INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2023-05-20 15:00:00', 3, 5, 'CONFIRMED');


INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2023-04-20 15:00:00', 3, 8, 'CONFIRMED');

INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2023-04-20 15:00:00', 3, 9, 'CONFIRMED');

INSERT INTO participation_requests
(created, event_id, requester_id, state)
VALUES('2023-06-20 15:00:00', 4, 1, 'CONFIRMED');
