INSERT INTO Users (username, password, email, name, lastName, createdAt, updatedAt, firstEntry, role) SELECT * FROM (VALUES
    ('admin', '$2a$10$tAX5UGkz3VvxhLe8.463oOuYMOXGFXB..pZzc2/sXXbOnJ2eWO2NO', 'admin@fd.com', 'admin', 'admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE,'ADMIN'),
    ('user1', '$2a$10$tAX5UGkz3VvxhLe8.463oOuYMOXGFXB..pZzc2/sXXbOnJ2eWO2NO', 'testUser@fd.com', 'Test', 'User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE,'USER')
) tmp WHERE NOT EXISTS ( SELECT * FROM Users );

INSERT INTO Category (name) SELECT * FROM (VALUES
    ('{"en": "Health", "es": "Salud", "gl": "Saúde"}'),
    ('{"en": "Education", "es": "Educación", "gl": "Educación"}'),
    ('{"en": "Wellness", "es": "Benestar", "gl": "Benestar"}'),
    ('{"en": "Fitness", "es": "Forma Física", "gl": "Forma Física"}'),
    ('{"en": "Lifestyle", "es": "Estilo de Vida", "gl": "Estilo de Vida"}'),
    ('{"en": "Hobbies", "es": "Aficións", "gl": "Aficións"}'),
    ('{"en": "Social", "es": "Social", "gl": "Social"}'),
    ('{"en": "Productivity", "es": "Productividad", "gl": "Produtividade"}'),
    ('{"en": "Personal Development", "es": "Desarrollo Personal", "gl": "Desenvolvemento Persoal"}')
) tmp WHERE NOT EXISTS ( SELECT * FROM Category );

INSERT INTO Habit (name, description, categoryId) SELECT * FROM (VALUES
    ('{"en": "Drink Water", "es": "Beber agua", "gl": "Beber auga"}', '{"en": "Stay hydrated by drinking plenty of water", "es": "Mantente hidratado bebiendo mucha agua", "gl": "Mantente hidratado bebendo moita auga"}', 1),
    ('{"en": "Exercise", "es": "Ejercicio", "gl": "Exercicio"}', '{"en": "Stay active with regular exercise", "es": "Mantente activo con ejercicio regular", "gl": "Mantente activo con exercicio regular"}', 4),
    ('{"en": "Read a Book", "es": "Leer un libro", "gl": "Ler un libro"}', '{"en": "Expand your knowledge by reading", "es": "Expande tu conocimiento leyendo", "gl": "Expande o teu coñecemento lendo"}', 2),
    ('{"en": "Meditate", "es": "Meditar", "gl": "Meditar"}', '{"en": "Practice mindfulness and meditation", "es": "Practica la atención plena y la meditación", "gl": "Practica a atención plena e a meditación"}', 3),
    ('{"en": "Get 8 Hours of Sleep", "es": "Dormir 8 horas", "gl": "Durmir 8 horas"}', '{"en": "Ensure adequate rest with 8 hours of sleep", "es": "Asegura un descanso adecuado con 8 horas de sueño", "gl": "Asegura un descanso adecuado con 8 horas de sono"}', 1),
    ('{"en": "Practice Gratitude", "es": "Practicar la gratitud", "gl": "Practicar a gratitude"}', '{"en": "Reflect on things you are grateful for", "es": "Reflexiona sobre las cosas por las que estás agradecido", "gl": "Reflexiona sobre as cousas polas que estás agradecido"}', 9),
    ('{"en": "Take a Walk", "es": "Dar un paseo", "gl": "Dar un paseo"}', '{"en": "Go for a walk to stay active and enjoy nature", "es": "Sal a caminar para mantenerte activo y disfrutar de la naturaleza", "gl": "Sae a camiñar para manterte activo e gozar da natureza"}', 4),
    ('{"en": "Call Family/Friends", "es": "Llamar a la familia/amigos", "gl": "Chamar á familia/amigos"}', '{"en": "Stay connected with loved ones", "es": "Mantente conectado con tus seres queridos", "gl": "Mantente conectado cos teus seres queridos"}', 7),
    ('{"en": "Learn Something New", "es": "Aprender algo nuevo", "gl": "Aprender algo novo"}', '{"en": "Dedicate time to learning a new skill or topic", "es": "Dedica tiempo a aprender una nueva habilidad o tema", "gl": "Dedica tempo a aprender unha nova habilidade ou tema"}', 2),
    ('{"en": "Deep Clean", "es": "Limpieza profunda", "gl": "Limpeza profunda"}', '{"en": "Thoroughly clean your living space", "es": "Limpia a fondo tu espacio vital", "gl": "Limpa a fondo o teu espazo vital"}', 5),
    ('{"en": "Hobby Time", "es": "Tiempo de pasatiempos", "gl": "Tempo de pasatempos"}', '{"en": "Spend time on your favorite hobby", "es": "Dedica tiempo a tu pasatiempo favorito", "gl": "Dedica tempo ao teu pasatempo favorito"}', 6),
    ('{"en": "Meal Prep", "es": "Preparar comidas", "gl": "Preparar comidas"}', '{"en": "Prepare healthy meals for the week", "es": "Prepara comidas saludables para la semana", "gl": "Prepara comidas saudables para a semana"}', 1),
    ('{"en": "Digital Detox", "es": "Desintoxicación digital", "gl": "Desintoxicación dixital"}', '{"en": "Take a break from digital devices", "es": "Tómate un descanso de los dispositivos digitales", "gl": "Tómate un descanso dos dispositivos dixitais"}', 3),
    ('{"en": "Review Goals", "es": "Revisar objetivos", "gl": "Revisar obxectivos"}', '{"en": "Reflect on and adjust your goals", "es": "Reflexiona y ajusta tus objetivos", "gl": "Reflexiona e axusta os teus obxectivos"}', 8),
    ('{"en": "Complete a Project", "es": "Completar un proyecto", "gl": "Completar un proxecto"}', '{"en": "Finish a personal or work project", "es": "Termina un proyecto personal o de trabajo", "gl": "Remata un proxecto persoal ou de traballo"}', 8),
    ('{"en": "Financial Review", "es": "Revisión financiera", "gl": "Revisión financeira"}', '{"en": "Review and plan your finances", "es": "Revisa y planifica tus finanzas", "gl": "Revisa e planifica as túas finanzas"}', 8),
    ('{"en": "Plan Adventure", "es": "Planificar aventura", "gl": "Planificar aventura"}', '{"en": "Plan an exciting activity or trip", "es": "Planifica una actividad emocionante o viaje", "gl": "Planifica unha actividade emocionante ou viaxe"}', 5),
    ('{"en": "Try New Recipe", "es": "Probar nueva receta", "gl": "Probar nova receita"}', '{"en": "Experiment with cooking a new dish", "es": "Experimenta cocinando un plato nuevo", "gl": "Experimenta cociñando un prato novo"}', 6),
    ('{"en": "Social Gathering", "es": "Reunión social", "gl": "Reunión social"}', '{"en": "Organize or attend a social event", "es": "Organiza o asiste a un evento social", "gl": "Organiza ou asiste a un evento social"}', 7),
    ('{"en": "Skill Assessment", "es": "Evaluación de habilidades", "gl": "Avaliación de habilidades"}', '{"en": "Assess and improve your skills", "es": "Evalúa y mejora tus habilidades", "gl": "Avalía e mellora as túas habilidades"}',  9)
) tmp WHERE NOT EXISTS ( SELECT * FROM Habit );

INSERT INTO Mood (name, image) SELECT * FROM (VALUES
    ('{"en": "Overjoyed", "es": "Lleno de alegría", "gl": "Cheo de alegría"}', 'data:image/svg;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgZmlsbD0iY3VycmVudENvbG9yIiBjbGFzcz0iYmkgYmktZW1vamktbGF1Z2hpbmciIHZpZXdCb3g9IjAgMCAxNiAxNiI+CiAgPHBhdGggZD0iTTggMTVBNyA3IDAgMSAxIDggMWE3IDcgMCAwIDEgMCAxNG0wIDFBOCA4IDAgMSAwIDggMGE4IDggMCAwIDAgMCAxNiIvPgogIDxwYXRoIGQ9Ik0xMi4zMzEgOS41YTEgMSAwIDAgMSAwIDFBNSA1IDAgMCAxIDggMTNhNSA1IDAgMCAxLTQuMzMtMi41QTEgMSAwIDAgMSA0LjUzNSA5aDYuOTNhMSAxIDAgMCAxIC44NjYuNU03IDYuNWMwIC44MjgtLjQ0OCAwLTEgMHMtMSAuODI4LTEgMFM1LjQ0OCA1IDYgNXMxIC42NzIgMSAxLjVtNCAwYzAgLjgyOC0uNDQ4IDAtMSAwcy0xIC44MjgtMSAwUzkuNDQ4IDUgMTAgNXMxIC42NzIgMSAxLjUiLz4KPC9zdmc+'),
    ('{"en": "Happy", "es": "Feliz", "gl": "Feliz"}', 'data:image/svg;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgZmlsbD0iY3VycmVudENvbG9yIiBjbGFzcz0iYmkgYmktZW1vamktc21pbGUiIHZpZXdCb3g9IjAgMCAxNiAxNiI+CiAgPHBhdGggZD0iTTggMTVBNyA3IDAgMSAxIDggMWE3IDcgMCAwIDEgMCAxNG0wIDFBOCA4IDAgMSAwIDggMGE4IDggMCAwIDAgMCAxNiIvPgogIDxwYXRoIGQ9Ik00LjI4NSA5LjU2N2EuNS41IDAgMCAxIC42ODMuMTgzQTMuNSAzLjUgMCAwIDAgOCAxMS41YTMuNSAzLjUgMCAwIDAgMy4wMzItMS43NS41LjUgMCAxIDEgLjg2Ni41QTQuNSA0LjUgMCAwIDEgOCAxMi41YTQuNSA0LjUgMCAwIDEtMy44OTgtMi4yNS41LjUgMCAwIDEgLjE4My0uNjgzTTcgNi41QzcgNy4zMjggNi41NTIgOCA2IDhzLTEtLjY3Mi0xLTEuNVM1LjQ0OCA1IDYgNXMxIC42NzIgMSAxLjVtNCAwYzAgLjgyOC0uNDQ4IDEuNS0xIDEuNXMtMS0uNjcyLTEtMS41UzkuNDQ4IDUgMTAgNXMxIC42NzIgMSAxLjUiLz4KPC9zdmc+'),
    ('{"en": "Neutral", "es": "Neutral", "gl": "Neutral"}','data:image/svg;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgZmlsbD0iY3VycmVudENvbG9yIiBjbGFzcz0iYmkgYmktZW1vamktbmV1dHJhbCIgdmlld0JveD0iMCAwIDE2IDE2Ij4KICA8cGF0aCBkPSJNOCAxNUE3IDcgMCAxIDEgOCAxYTcgNyAwIDAgMSAwIDE0bTAgMUE4IDggMCAxIDAgOCAwYTggOCAwIDAgMCAwIDE2Ii8+CiAgPHBhdGggZD0iTTQgMTAuNWEuNS41IDAgMCAwIC41LjVoN2EuNS41IDAgMCAwIDAtMWgtN2EuNS41IDAgMCAwLS41LjVtMy00QzcgNS42NzIgNi41NTIgNSA2IDVzLTEgLjY3Mi0xIDEuNVM1LjQ0OCA4IDYgOHMxLS42NzIgMS0xLjVtNCAwYzAtLjgyOC0uNDQ4LTEuNS0xLTEuNXMtMSAuNjcyLTEgMS41UzkuNDQ4IDggMTAgOHMxLS42NzIgMS0xLjUiLz4KPC9zdmc+'),
    ('{"en": "Sad", "es": "Triste", "gl": "Triste"}', 'data:image/svg;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgZmlsbD0iY3VycmVudENvbG9yIiBjbGFzcz0iYmkgYmktZW1vamktZnJvd24iIHZpZXdCb3g9IjAgMCAxNiAxNiI+CiAgPHBhdGggZD0iTTggMTVBNyA3IDAgMSAxIDggMWE3IDcgMCAwIDEgMCAxNG0wIDFBOCA4IDAgMSAwIDggMGE4IDggMCAwIDAgMCAxNiIvPgogIDxwYXRoIGQ9Ik00LjI4NSAxMi40MzNhLjUuNSAwIDAgMCAuNjgzLS4xODNBMy41IDMuNSAwIDAgMSA4IDEwLjVjMS4yOTUgMCAyLjQyNi43MDMgMy4wMzIgMS43NWEuNS41IDAgMCAwIC44NjYtLjVBNC41IDQuNSAwIDAgMCA4IDkuNWE0LjUgNC41IDAgMCAwLTMuODk4IDIuMjUuNS41IDAgMCAwIC4xODMuNjgzTTcgNi41QzcgNy4zMjggNi41NTIgOCA2IDhzLTEtLjY3Mi0xLTEuNVM1LjQ0OCA1IDYgNXMxIC42NzIgMSAxLjVtNCAwYzAgLjgyOC0uNDQ4IDEuNS0xIDEuNXMtMS0uNjcyLTEtMS41UzkuNDQ4IDUgMTAgNXMxIC42NzIgMSAxLjUiLz4KPC9zdmc+'),
    ('{"en": "Depressed", "es": "Deprimido", "gl": "Deprimido"}', 'data:image/svg;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiIgZmlsbD0iY3VycmVudENvbG9yIiBjbGFzcz0iYmkgYmktZW1vamktdGVhciIgdmlld0JveD0iMCAwIDE2IDE2Ij4KICA8cGF0aCBkPSJNOCAxNUE3IDcgMCAxIDEgOCAxYTcgNyAwIDAgMSAwIDE0bTAgMUE4IDggMCAxIDAgOCAwYTggOCAwIDAgMCAwIDE2Ii8+CiAgPHBhdGggZD0iTTYuODMxIDExLjQzQTMuMSAzLjEgMCAwIDEgOCAxMS4xOTZjLjkxNiAwIDEuNjA3LjQwOCAyLjI1LjgyNi4yMTIuMTM4LjQyNC0uMDY5LjI4Mi0uMjc3LS41NjQtLjgzLTEuNTU4LTIuMDQ5LTIuNTMyLTIuMDQ5LS41MyAwLTEuMDY2LjM2MS0xLjUzNi44MjRxLjEyNi4yNy4yMzIuNTM1LjA2OS4xNzQuMTM1LjM3M1pNNiAxMS4zMzNDNiAxMi4yNTMgNS4zMjggMTMgNC41IDEzUzMgMTIuMjU0IDMgMTEuMzMzYzAtLjcwNi44ODItMi4yOSAxLjI5NC0yLjk5YS4yMzguMjM4IDAgMCAxIC40MTIgMGMuNDEyLjcgMS4yOTQgMi4yODQgMS4yOTQgMi45OU03IDYuNUM3IDcuMzI4IDYuNTUyIDggNiA4cy0xLS42NzItMS0xLjVTNS40NDggNSA2IDVzMSAuNjcyIDEgMS41bTQgMGMwIC44MjgtLjQ0OCAxLjUtMSAxLjVzLTEtLjY3Mi0xLTEuNVM5LjQ0OCA1IDEwIDVzMSAuNjcyIDEgMS41bS0xLjUtM0EuNS41IDAgMCAxIDEwIDNjMS4xNjIgMCAyLjM1LjU4NCAyLjk0NyAxLjc3NmEuNS41IDAgMSAxLS44OTQuNDQ4QzExLjY0OSA0LjQxNiAxMC44MzggNCAxMCA0YS41LjUgMCAwIDEtLjUtLjVNNyAzLjVhLjUuNSAwIDAgMC0uNS0uNWMtMS4xNjIgMC0yLjM1LjU4NC0yLjk0NyAxLjc3NmEuNS41IDAgMSAwIC44OTQuNDQ4QzQuODUxIDQuNDE2IDUuNjYyIDQgNi41IDRhLjUuNSAwIDAgMCAuNS0uNSIvPgo8L3N2Zz4=')
) tmp WHERE NOT EXISTS ( SELECT * FROM Mood );

INSERT INTO Trophy (name, description, image, days) SELECT * FROM (VALUES
    ('{"en": "One week streak", "es": "Una semana de racha", "gl": "Unha semana de racha"}', '{"en": "Awarded for completing a habit for 7 consecutive days", "es": "Una semana de racha", "gl": "Unha semana de racha"}', 'one_week_streak.jpg', 7),
    ('{"en": "Two weeks streak", "es": "Dos semanas de racha", "gl": "Dúas semanas de racha"}', '{"en": "Awarded for completing a habit for 14 consecutive days", "es": "Dos semanas de racha", "gl": "Dúas semanas de racha"}', 'two_weeks_streak.jpg', 14),
    ('{"en": "One month streak", "es": "Un mes de racha", "gl": "Un mes de racha"}', '{"en": "Awarded for completing a habit for 30 consecutive days", "es": "Un mes de racha", "gl": "Un mes de racha"}', 'one_month_streak.jpg', 30),
    ('{"en": "Three months streak", "es": "Tres meses de racha", "gl": "Tres meses de racha"}', '{"en": "Awarded for completing a habit for 90 consecutive days", "es": "Tres meses de racha", "gl": "Tres meses de racha"}', 'three_month_streak.jpg', 90),
    ('{"en": "Six months streak", "es": "Seis meses de racha", "gl": "Seis meses de racha"}', '{"en": "Awarded for completing a habit for 180 consecutive days", "es": "Seis meses de racha", "gl": "Seis meses de racha"}', 'six_month_streak.jpg', 180),
    ('{"en": "One year streak", "es": "Un ano de racha", "gl": "Un ano de racha"}', '{"en": "Awarded for completing a habit for 365 consecutive days", "es": "Un ano de racha", "gl": "Un ano de racha"}', 'one_year_streak.jpg', 365)
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