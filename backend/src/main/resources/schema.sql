CREATE TABLE IF NOT EXISTS Users (
    id Long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    firstEntry BOOLEAN NOT NULL DEFAULT TRUE,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS Mood (
    id Long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image VARCHAR(255)  
);

CREATE TABLE IF NOT EXISTS DiaryEntry (
    id Long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    userId Long NOT NULL,
    content VARCHAR(255) NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    moodId Long NOT NULL,
    CONSTRAINT fk_diary_entry_user FOREIGN KEY (userId) REFERENCES Users(id),
    CONSTRAINT fk_diary_entry_mood FOREIGN KEY (moodId) REFERENCES Mood(id)
);

CREATE TABLE IF NOT EXISTS Images (
    id Long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    image BLOB NOT NULL,
    uploadDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    diaryEntryId Long NOT NULL,
    CONSTRAINT fk_image_diary_entry FOREIGN KEY (diaryEntryId) REFERENCES DiaryEntry(id)
);

CREATE TABLE IF NOT EXISTS Category (
    id Long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Habit (
    id Long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL,
    categoryId Long NOT NULL,
    CONSTRAINT fk_habit_category FOREIGN KEY (categoryId) REFERENCES Category(id)
);

CREATE TABLE IF NOT EXISTS UserHabit (
    id Long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    userId Long NOT NULL,
    habitId Long NOT NULL,
    CONSTRAINT fk_user_habit_user FOREIGN KEY (userId) REFERENCES Users(id),
    CONSTRAINT fk_user_habit_habit FOREIGN KEY (habitId) REFERENCES Habit(id)
);

CREATE TABLE IF NOT EXISTS HabitEntry (
    id Long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    streak int NOT NULL DEFAULT 0,
    userId Long NOT NULL,
    userHabitId Long NOT NULL,
    diaryEntryId Long NOT NULL,
    CONSTRAINT fk_habit_entry_user FOREIGN KEY (userId) REFERENCES Users(id),
    CONSTRAINT fk_habit_entry_user_habit FOREIGN KEY (userHabitId) REFERENCES UserHabit(id),
    CONSTRAINT fk_habit_entry_diary_entry FOREIGN KEY (diaryEntryId) REFERENCES DiaryEntry(id)
);


CREATE TABLE IF NOT EXISTS Trophy (
    id Long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    days int NOT NULL,
    image VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS UserTrophy (
    id Long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    obtainedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    userId Long NOT NULL,
    trophyId Long NOT NULL,
    habitId Long NOT NULL,
    CONSTRAINT fk_user_trophy_habit FOREIGN KEY (habitId) REFERENCES Habit(id),
    CONSTRAINT fk_user_trophy_user FOREIGN KEY (userId) REFERENCES Users(id),
    CONSTRAINT fk_user_trophy_trophy FOREIGN KEY (trophyId) REFERENCES Trophy(id)
);

CREATE TABLE IF NOT EXISTS WeeklySummary (
    id Long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    habitsCompleted int NOT NULL DEFAULT 0,
    biggestStreak int NOT NULL DEFAULT 0,
    moodTrendId Long NOT NULL,
    totalEntries int NOT NULL DEFAULT 0,
    trophiesObtained int NOT NULL DEFAULT 0,
    userId Long NOT NULL,
    CONSTRAINT fk_weekly_summary_user FOREIGN KEY (userId) REFERENCES Users(id),
    CONSTRAINT fk_weekly_summary_mood_trend FOREIGN KEY (moodTrendId) REFERENCES Mood(id)
);

INSERT INTO Mood(name, image) VALUES ('Happy', null);