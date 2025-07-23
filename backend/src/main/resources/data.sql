INSERT INTO Users (userName, password, email, name, lastName, createdAt, updatedAt, role) SELECT * FROM (VALUES
    ('admin', '$2a$10$tAX5UGkz3VvxhLe8.463oOuYMOXGFXB..pZzc2/sXXbOnJ2eWO2NO', 'admin@fd.com', 'admin', 'admin', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ADMIN')
) tmp WHERE NOT EXISTS ( SELECT * FROM Users );

INSERT INTO Category (name) SELECT * FROM (VALUES
    ('Health'),
    ('Productivity'),
    ('Personal Development')
) tmp WHERE NOT EXISTS ( SELECT * FROM Category );

INSERT INTO Habit (name, description, image, categoryId) SELECT * FROM (VALUES
    ('Drinking Water', 'Drink 2 liters of water daily', 'water.jpg', 1),
    ('Exercise', '30 minutes of exercise daily', 'exercise.jpg', 1),
    ('Reading', 'Read 20 pages of a book daily', 'reading.jpg', 2),
    ('Meditation', 'Meditate for 10 minutes daily', 'meditation.jpg', 2),
    ('Journaling', 'Write in a journal every night', 'journaling.jpg', 3),
    ('Learning a Language', 'Practice a new language for 15 minutes daily', 'language.jpg', 3)
) tmp WHERE NOT EXISTS ( SELECT * FROM Habit );

