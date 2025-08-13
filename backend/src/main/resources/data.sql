INSERT INTO Users (userName, password, email, name, lastName, createdAt, updatedAt, firstEntry, role) SELECT * FROM (VALUES
    ('admin', '$2a$10$tAX5UGkz3VvxhLe8.463oOuYMOXGFXB..pZzc2/sXXbOnJ2eWO2NO', 'admin@fd.com', 'admin', 'admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE,'ADMIN'),
    ('user1', '$2a$10$tAX5UGkz3VvxhLe8.463oOuYMOXGFXB..pZzc2/sXXbOnJ2eWO2NO', 'testUser@fd.com', 'Test', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE,'USER')
) tmp WHERE NOT EXISTS ( SELECT * FROM Users );

INSERT INTO Category (nameEn, nameEs, nameGl) SELECT * FROM (VALUES
    ('Health', 'Salud', 'Saúde'),
    ('Education', 'Educación', 'Educación'),
    ('Wellness', 'Benestar', 'Benestar'),
    ('Fitness', 'Forma Física', 'Forma Física'),
    ('Lifestyle', 'Estilo de Vida', 'Estilo de Vida'),
    ('Hobbies', 'Aficións', 'Aficións'),
    ('Social', 'Social', 'Social'),
    ('Productivity', 'Productividad', 'Produtividade'),
    ('Personal Development', 'Desarrollo Personal', 'Desenvolvemento Persoal')
) tmp WHERE NOT EXISTS ( SELECT * FROM Category );

INSERT INTO Habit (nameEn, nameEs, nameGl, descriptionEn, descriptionEs, descriptionGl, image, categoryId) SELECT * FROM (VALUES
    ('Drinking Water', 'Beber agua', 'Beber auga', 'Drink 2 liters of water daily', 'Beber 2 litros de agua diariamente', 'Beber 2 litros de auga diariamente', 'water.jpg', 1),
    ('Exercise', 'Ejercicio', 'Exercicio', '30 minutes of exercise daily', '30 minutos de ejercicio diario', '30 minutos de exercicio diario', 'exercise.jpg', 1),
    ('Reading', 'Lectura', 'Lectura', 'Read 20 pages of a book daily', 'Leer 20 páginas de un libro diariamente', 'Ler 20 páxinas dun libro diariamente', 'reading.jpg', 2),
    ('Meditation', 'Meditación', 'Meditación', 'Meditate for 10 minutes daily', 'Meditar durante 10 minutos diariamente', 'Meditar durante 10 minutos diariamente', 'meditation.jpg', 2),
    ('Journaling', 'Escritura en diario', 'Escritura en diario', 'Write in a journal every night', 'Escribir en un diario cada noche', 'Escribir nun diario cada noite', 'journaling.jpg', 3),
    ('Learning a Language', 'Aprender un idioma', 'Aprender un idioma', 'Practice a new language for 15 minutes daily', 'Practicar un nuevo idioma durante 15 minutos al día', 'Practicar un novo idioma durante 15 minutos ao día', 'language.jpg', 3)
) tmp WHERE NOT EXISTS ( SELECT * FROM Habit );

INSERT INTO Mood (nameEn, nameEs, nameGl, image) SELECT * FROM (VALUES
    ('Overjoyed', 'Lleno de alegría', 'Cheo de alegría', 'data:image/svg;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgZmlsbD0iY3VycmVudENvbG9yIiBjbGFzcz0iYmkgYmktZW1vamktbGF1Z2hpbmciIHZpZXdCb3g9IjAgMCAxNiAxNiI+CiAgPHBhdGggZD0iTTggMTVBNyA3IDAgMSAxIDggMWE3IDcgMCAwIDEgMCAxNG0wIDFBOCA4IDAgMSAwIDggMGE4IDggMCAwIDAgMCAxNiIvPgogIDxwYXRoIGQ9Ik0xMi4zMzEgOS41YTEgMSAwIDAgMSAwIDFBNSA1IDAgMCAxIDggMTNhNSA1IDAgMCAxLTQuMzMtMi41QTEgMSAwIDAgMSA0LjUzNSA5aDYuOTNhMSAxIDAgMCAxIC44NjYuNU03IDYuNWMwIC44MjgtLjQ0OCAwLTEgMHMtMSAuODI4LTEgMFM1LjQ0OCA1IDYgNXMxIC42NzIgMSAxLjVtNCAwYzAgLjgyOC0uNDQ4IDAtMSAwcy0xIC44MjgtMSAwUzkuNDQ4IDUgMTAgNXMxIC42NzIgMSAxLjUiLz4KPC9zdmc+'),
    ('Happy', 'Feliz', 'Feliz', 'data:image/svg;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgZmlsbD0iY3VycmVudENvbG9yIiBjbGFzcz0iYmkgYmktZW1vamktc21pbGUiIHZpZXdCb3g9IjAgMCAxNiAxNiI+CiAgPHBhdGggZD0iTTggMTVBNyA3IDAgMSAxIDggMWE3IDcgMCAwIDEgMCAxNG0wIDFBOCA4IDAgMSAwIDggMGE4IDggMCAwIDAgMCAxNiIvPgogIDxwYXRoIGQ9Ik00LjI4NSA5LjU2N2EuNS41IDAgMCAxIC42ODMuMTgzQTMuNSAzLjUgMCAwIDAgOCAxMS41YTMuNSAzLjUgMCAwIDAgMy4wMzItMS43NS41LjUgMCAxIDEgLjg2Ni41QTQuNSA0LjUgMCAwIDEgOCAxMi41YTQuNSA0LjUgMCAwIDEtMy44OTgtMi4yNS41LjUgMCAwIDEgLjE4My0uNjgzTTcgNi41QzcgNy4zMjggNi41NTIgOCA2IDhzLTEtLjY3Mi0xLTEuNVM1LjQ0OCA1IDYgNXMxIC42NzIgMSAxLjVtNCAwYzAgLjgyOC0uNDQ4IDEuNS0xIDEuNXMtMS0uNjcyLTEtMS41UzkuNDQ4IDUgMTAgNXMxIC42NzIgMSAxLjUiLz4KPC9zdmc+'),
    ('Neutral', 'Neutral', 'Neutral','data:image/svg;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgZmlsbD0iY3VycmVudENvbG9yIiBjbGFzcz0iYmkgYmktZW1vamktbmV1dHJhbCIgdmlld0JveD0iMCAwIDE2IDE2Ij4KICA8cGF0aCBkPSJNOCAxNUE3IDcgMCAxIDEgOCAxYTcgNyAwIDAgMSAwIDE0bTAgMUE4IDggMCAxIDAgOCAwYTggOCAwIDAgMCAwIDE2Ii8+CiAgPHBhdGggZD0iTTQgMTAuNWEuNS41IDAgMCAwIC41LjVoN2EuNS41IDAgMCAwIDAtMWgtN2EuNS41IDAgMCAwLS41LjVtMy00QzcgNS42NzIgNi41NTIgNSA2IDVzLTEgLjY3Mi0xIDEuNVM1LjQ0OCA4IDYgOHMxLS42NzIgMS0xLjVtNCAwYzAtLjgyOC0uNDQ4LTEuNS0xLTEuNXMtMSAuNjcyLTEgMS41UzkuNDQ4IDggMTAgOHMxLS42NzIgMS0xLjUiLz4KPC9zdmc+'),
    ('Sad', 'Triste', 'Triste', 'data:image/svg;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgZmlsbD0iY3VycmVudENvbG9yIiBjbGFzcz0iYmkgYmktZW1vamktZnJvd24iIHZpZXdCb3g9IjAgMCAxNiAxNiI+CiAgPHBhdGggZD0iTTggMTVBNyA3IDAgMSAxIDggMWE3IDcgMCAwIDEgMCAxNG0wIDFBOCA4IDAgMSAwIDggMGE4IDggMCAwIDAgMCAxNiIvPgogIDxwYXRoIGQ9Ik00LjI4NSAxMi40MzNhLjUuNSAwIDAgMCAuNjgzLS4xODNBMy41IDMuNSAwIDAgMSA4IDEwLjVjMS4yOTUgMCAyLjQyNi43MDMgMy4wMzIgMS43NWEuNS41IDAgMCAwIC44NjYtLjVBNC41IDQuNSAwIDAgMCA4IDkuNWE0LjUgNC41IDAgMCAwLTMuODk4IDIuMjUuNS41IDAgMCAwIC4xODMuNjgzTTcgNi41QzcgNy4zMjggNi41NTIgOCA2IDhzLTEtLjY3Mi0xLTEuNVM1LjQ0OCA1IDYgNXMxIC42NzIgMSAxLjVtNCAwYzAgLjgyOC0uNDQ4IDEuNS0xIDEuNXMtMS0uNjcyLTEtMS41UzkuNDQ4IDUgMTAgNXMxIC42NzIgMSAxLjUiLz4KPC9zdmc+'),
    ('Depressed', 'Deprimido', 'Deprimido', 'data:image/svg;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgZmlsbD0iY3VycmVudENvbG9yIiBjbGFzcz0iYmkgYmktZW1vamktdGVhciIgdmlld0JveD0iMCAwIDE2IDE2Ij4KICA8cGF0aCBkPSJNOCAxNUE3IDcgMCAxIDEgOCAxYTcgNyAwIDAgMSAwIDE0bTAgMUE4IDggMCAxIDAgOCAwYTggOCAwIDAgMCAwIDE2Ii8+CiAgPHBhdGggZD0iTTYuODMxIDExLjQzQTMuMSAzLjEgMCAwIDEgOCAxMS4xOTZjLjkxNiAwIDEuNjA3LjQwOCAyLjI1LjgyNi4yMTIuMTM4LjQyNC0uMDY5LjI4Mi0uMjc3LS41NjQtLjgzLTEuNTU4LTIuMDQ5LTIuNTMyLTIuMDQ5LS41MyAwLTEuMDY2LjM2MS0xLjUzNi44MjRxLjEyNi4yNy4yMzIuNTM1LjA2OS4xNzQuMTM1LjM3M1pNNiAxMS4zMzNDNiAxMi4yNTMgNS4zMjggMTMgNC41IDEzUzMgMTIuMjU0IDMgMTEuMzMzYzAtLjcwNi44ODItMi4yOSAxLjI5NC0yLjk5YS4yMzguMjM4IDAgMCAxIC40MTIgMGMuNDEyLjcgMS4yOTQgMi4yODQgMS4yOTQgMi45OU03IDYuNUM3IDcuMzI4IDYuNTUyIDggNiA4cy0xLS42NzItMS0xLjVTNS40NDggNSA2IDVzMSAuNjcyIDEgMS41bTQgMGMwIC44MjgtLjQ0OCAxLjUtMSAxLjVzLTEtLjY3Mi0xLTEuNVM5LjQ0OCA1IDEwIDVzMSAuNjcyIDEgMS41bS0xLjUtM0EuNS41IDAgMCAxIDEwIDNjMS4xNjIgMCAyLjM1LjU4NCAyLjk0NyAxLjc3NmEuNS41IDAgMSAxLS44OTQuNDQ4QzExLjY0OSA0LjQxNiAxMC44MzggNCAxMCA0YS41LjUgMCAwIDEtLjUtLjVNNyAzLjVhLjUuNSAwIDAgMC0uNS0uNWMtMS4xNjIgMC0yLjM1LjU4NC0yLjk0NyAxLjc3NmEuNS41IDAgMSAwIC44OTQuNDQ4QzQuODUxIDQuNDE2IDUuNjYyIDQgNi41IDRhLjUuNSAwIDAgMCAuNS0uNSIvPgo8L3N2Zz4=')
) tmp WHERE NOT EXISTS ( SELECT * FROM Mood );

INSERT INTO Trophy (nameEn, nameEs, nameGl, descriptionEn, descriptionEs, descriptionGl, image, days) SELECT * FROM (VALUES
    ('One week streak', 'Una semana de racha', 'Unha semana de racha', 'Awarded for completing a habit for 7 consecutive days', 'Una semana de racha', 'Unha semana de racha', 'one_week_streak.jpg', 7),
    ('Two weeks streak', 'Dos semanas de racha', 'Dúas semanas de racha', 'Awarded for completing a habit for 14 consecutive days', 'Dos semanas de racha', 'Dúas semanas de racha', 'two_weeks_streak.jpg', 14),
    ('One month streak', 'Un mes de racha', 'Un mes de racha', 'Awarded for completing a habit for 30 consecutive days', 'Un mes de racha', 'Un mes de racha', 'one_month_streak.jpg', 30),
    ('Three months streak', 'Tres meses de racha', 'Tres meses de racha', 'Awarded for completing a habit for 90 consecutive days', 'Tres meses de racha', 'Tres meses de racha', 'three_month_streak.jpg', 90),
    ('Six months streak', 'Seis meses de racha', 'Seis meses de racha', 'Awarded for completing a habit for 180 consecutive days', 'Seis meses de racha', 'Seis meses de racha', 'six_month_streak.jpg', 180),
    ('One year streak', 'Un ano de racha', 'Un ano de racha', 'Awarded for completing a habit for 365 consecutive days', 'Un ano de racha', 'Un ano de racha', 'one_year_streak.jpg', 365)
) tmp WHERE NOT EXISTS ( SELECT * FROM Trophy );

-- TEST DATA

INSERT INTO UserHabit (userId, habitId) SELECT * FROM (VALUES
    (2, 1),
    (2, 2),
    (2, 3)
) tmp WHERE NOT EXISTS ( SELECT * FROM UserHabit );

INSERT INTO DiaryEntry (userId, content, date, moodId) SELECT * FROM (VALUES
    (2, 'I felt a bit overwhelmed today but managed to get through it.', DATEADD('DAY', -30, CURRENT_TIMESTAMP), 1),
    (2, 'I had a productive day, completed all my tasks and felt great.', DATEADD('DAY', -60, CURRENT_TIMESTAMP), 2),
    (2, 'Today I felt very happy and accomplished.', DATEADD('DAY', -6, CURRENT_TIMESTAMP), 4),
    (2, 'I was a bit down today but managed to stay positive.', DATEADD('DAY', -5, CURRENT_TIMESTAMP), 1),
    (2, 'Had a frustrating day at work but I managed to relax in the evening.', DATEADD('DAY', -4, CURRENT_TIMESTAMP), 2),
    (2, 'Felt anxious about the upcoming exam but did some meditation to calm down.', DATEADD('DAY', -3, CURRENT_TIMESTAMP), 5),
    (2, 'Today was a very relaxing day, spent time with family and friends.', DATEADD('DAY', -2, CURRENT_TIMESTAMP), 3),
    (2, 'I felt very productive today, completed all my tasks and even had time to read.', DATEADD('DAY', -1, CURRENT_TIMESTAMP), 4)
) tmp WHERE NOT EXISTS ( SELECT * FROM DiaryEntry );

INSERT INTO HabitEntry (date, streak, userId, habitId, diaryEntryId) SELECT * FROM (VALUES
    (DATEADD('DAY', -6, CURRENT_TIMESTAMP), 1, 2, 1, 3),
    (DATEADD('DAY', -5, CURRENT_TIMESTAMP), 2, 2, 1, 4),
    (DATEADD('DAY', -4, CURRENT_TIMESTAMP), 3, 2, 1, 5),
    (DATEADD('DAY', -3, CURRENT_TIMESTAMP), 4, 2, 1, 6),
    (DATEADD('DAY', -2, CURRENT_TIMESTAMP), 5, 2, 1, 7),
    (DATEADD('DAY', -1, CURRENT_TIMESTAMP), 6, 2, 1, 8)
) tmp WHERE NOT EXISTS ( SELECT * FROM HabitEntry );