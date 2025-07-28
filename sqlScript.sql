CREATE TABLE `muscle_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `muscle_group_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `muscle_group_name_UNIQUE` (`muscle_group_name`)
);

CREATE TABLE `exercise` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
);

CREATE TABLE `sets` (
  `id_sets` int NOT NULL AUTO_INCREMENT,
  `reps` int NOT NULL,
  `kg` float(10,2) NOT NULL,
  `set_number` int NOT NULL,
  `workout_details_id` int NOT NULL,
  PRIMARY KEY (`id_sets`),
  KEY `stats_workout_details` (`workout_details_id`),
  CONSTRAINT `stats_workout_details` FOREIGN KEY (`workout_details_id`) REFERENCES `workout_details` (`id`)
);

CREATE TABLE `workout_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `workout_id` int NOT NULL,
  `exercise_id` int NOT NULL,
  `muscle_group_id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_workoutDetails_exercise` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`id`),
  CONSTRAINT `fk_workoutDetails_muscleGroup` FOREIGN KEY (`muscle_group_id`) REFERENCES `muscle_group` (`id`),
  CONSTRAINT `workout_details_workout_fk` FOREIGN KEY (`workout_id`) REFERENCES `workout` (`id_workout`)
);

CREATE TABLE `workout` (
  `id_workout` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `date` date NOT NULL,
  `workout_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id_workout`),
  CONSTRAINT `workout_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id_user`)
);

CREATE TABLE `user` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` char(80) NOT NULL,
  `additional_info_id` int DEFAULT NULL,
  `role_id` int NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `additional_info_id_UNIQUE` (`additional_info_id`),
  KEY `user_role_fk` (`role_id`),
  CONSTRAINT `fk_user_additional_info` FOREIGN KEY (`additional_info_id`) REFERENCES `additional_info` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_role_fk` FOREIGN KEY (`role_id`) REFERENCES `role` (`id_role`)
);

CREATE TABLE `role` (
  `id_role` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) NOT NULL
  );

CREATE TABLE `additional_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `city_id` int NOT NULL,
  `street_info` varchar(65) NOT NULL,
  `cm` int DEFAULT NULL,
  `kg` float(7,2) DEFAULT NULL,
  `born_date` date DEFAULT NULL,
  `gender` tinyint(1) DEFAULT NULL,
   CONSTRAINT `additional_city_fk` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
);

CREATE TABLE `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `city_name` varchar(45) NOT NULL
  );

INSERT INTO workout (user_id, date, workout_name) VALUES
( 1, '2023-10-01', 'FBW'),
( 1, '2023-10-02', 'UBW'),
( 2, '2023-10-03', 'LBW'),
( 2, '2023-10-04', 'Cardio'),
( 3, '2023-10-05', 'Yoga'),
( 3, '2023-10-06', 'Strength'),
( 4, '2023-10-07', 'Core'),
( 4, '2023-10-08', 'HIIT'),
( 5, '2023-10-09', 'Pilates'),
( 5, '2023-10-10', 'Crossfit'),
( 6, '2023-10-11', 'Running'),
( 6, '2023-10-12', 'Cycling'),
( 7, '2023-10-13', 'Swimming'),
( 7, '2023-10-14', 'Boxing'),
( 8, '2023-10-15', 'Dance'),
( 8, '2023-10-16', 'Zumba'),
( 9, '2023-10-17', 'Stretching'),
( 9, '2023-10-18', 'Meditation'),
( 10, '2023-10-19', 'Kickboxing'),
( 10, '2023-10-20', 'Martial Arts');

INSERT INTO workout_details (exercise_id, weight) VALUES
(1, 1, 50),
(1, 2, 30),
(2, 3, 40),
(2, 4, 20),
(3, 5, 60),
(3, 6, 25),
(4, 7, 35),
(4, 8, 45),
(5, 9, 55),
( 5, 10, 15),
( 6, 11, 65),
( 6, 12, 10),
( 7, 13, 70),
( 7, 14, 5),
( 8, 15, 75),
( 8, 16, 80),
( 9, 17, 85),
( 9, 18, 90),
( 10, 19, 95),
( 10, 20, 100);

INSERT INTO sets (reps, kg, set_number, workout_details_id) VALUES
(10, 50.00, 1, 1),
(12, 30.00, 2, 1),
(8, 40.00, 1, 2),
(15, 20.00, 2, 2),
(10, 60.00, 1, 3),
(12, 25.00, 2, 3),
(8, 35.00, 1, 4),
(15, 45.00, 2, 4),
(10, 55.00, 1, 5),
(12, 15.00, 2, 5),
(8, 65.00, 1, 6),
(15, 10.00, 2, 6),
(10, 70.00, 1, 7),
(12, 5.00, 2, 7),
(8, 75.00, 1, 8),
(15, 80.00, 2, 8),
(10, 85.00, 1, 9),
(12, 90.00, 2, 9),
(8, 95.00, 1, 10),
(15, 100.00, 2, 10);

INSERT INTO exercise (name) VALUES
(1, 'Bench Press'),
(2, 'Squat'),
(3, 'Deadlift'),
(4, 'Pull-Up'),
(5, 'Push-Up'),
(6, 'Lunges'),
(7, 'Plank'),
(8, 'Bicep Curl'),
(9, 'Tricep Dip'),
(10, 'Leg Press'),
(11, 'Chest Fly'),
(12, 'Shoulder Press'),
(13, 'Lat Pulldown'),
(14, 'Cable Row'),
(15, 'Leg Curl'),
(16, 'Calf Raise'),
(17, 'Russian Twist'),
(18, 'Mountain Climber'),
(19, 'Burpee'),
(20, 'Jump Squat');

INSERT INTO muscle_group (muscle_group_name) VALUES
('Chest'),
('Legs'),
('Back'),
('Lats'),
('Core'),
('Biceps'),
('Triceps'),
('Shoulders'),
('Full Body');

INSERT INTO city (city_name) VALUES
('New York'),
('Los Angeles'),
('Chicago'),
('Houston'),
('Phoenix'),
('Philadelphia'),
('San Antonio'),
('San Diego'),
('Dallas'),
('San Jose'),
('Austin'),
('Jacksonville'),
('Fort Worth'),
('Columbus'),
('Charlotte'),
('San Francisco'),
('Indianapolis'),
('Seattle'),
('Denver'),
('Washington');

INSERT INTO additional_info (first_name, last_name, city_id, street_info, cm, kg, born_date, gender) VALUES
('John', 'Doe', 1, '123 Main St', 180, 80.5, '1990-01-01', 1),
('Jane', 'Smith', 2, '456 Elm St', 165, 60.0, '1992-02-02', 0),
('Alice', 'Johnson', 3, '789 Oak St', 170, 65.0, '1985-03-03', 0),
('Bob', 'Brown', 4, '101 Pine St', 175, 70.0, '1988-04-04', 1),
('Charlie', 'Davis', 5, '202 Maple St', 160, 55.0, '1995-05-05', 1),
('Diana', 'Evans', 6, '303 Birch St', 168, 58.0, '1993-06-06', 0),
('Eve', 'Harris', 7, '404 Cedar St', 172, 62.0, '1991-07-07', 0),
('Frank', 'Wilson', 8, '505 Spruce St', 178, 75.0, '1987-08-08', 1),
('Grace', 'Lee', 9, '606 Fir St', 162, 57.0, '1994-09-09', 0),
('Henry', 'Clark', 10, '707 Redwood St', 185, 85.0, '1986-10-10', 1),
('Ivy', 'Lewis', 11, '808 Willow St', 167, 59.0, '1996-11-11', 0),
('Jack', 'Walker', 12, '909 Aspen St', 174, 72.0, '1989-12-12', 1),
('Karen', 'Hall', 13, '1010 Birch St', 169, 61.0, '1997-01-13', 0),
('Leo', 'Allen', 14, '1111 Cedar St', 176, 77.0, '1984-02-14', 1),
('Mia', 'Young', 15, '1212 Elm St', 163, 56.0, '1998-03-15', 0),
('Noah', 'King', 16, '1313 Oak St', 179, 79.0, '1983-04-16', 1),
('Olivia', 'Wright', 17, '1414 Pine St', 164, 58.5, '1999-05-17', 0),
('Peter', 'Scott', 18, '1515 Maple St', 177, 76.0, '1982-06-18', 1),
('Quinn', 'Green', 19, '1616 Spruce St', 166, 60.5, '2000-07-19', 0),
('Rachel', 'Adams', 20, '1717 Fir St', 173, 68.0, '1981-08-20', 1);

INSERT INTO role(role_name) VALUES
("Beton"),
("Kamuk");

INSERT INTO user (username, password, additional_info_id, role_id, email) VALUES
('johndoe', 'password1', 1, 1, 'johndoe@example.com'),
('janesmith', 'password2', 2, 2, 'janesmith@example.com'),
('alicejohnson', 'password3', 3, 2, 'alicejohnson@example.com'),
('bobbrown', 'password4', 4, 2, 'bobbrown@example.com'),
('charliedavis', 'password5', 5, 2, 'charliedavis@example.com'),
('dianaevans', 'password6', 6, 2, 'dianaevans@example.com'),
('eveharris', 'password7', 7, 2, 'eveharris@example.com'),
('frankwilson', 'password8', 8, 2, 'frankwilson@example.com'),
('gracelee', 'password9', 9, 2, 'gracelee@example.com'),
('henryclark', 'password10', 10, 2, 'henryclark@example.com'),
('ivylewis', 'password11', 11, 2, 'ivylewis@example.com'),
('jackwalker', 'password12', 12, 2, 'jackwalker@example.com'),
('karenhall', 'password13', 13, 2, 'karenhall@example.com'),
('leoallen', 'password14', 14, 2, 'leoallen@example.com'),
('miayoung', 'password15', 15, 2, 'miayoung@example.com'),
('noahking', 'password16', 16, 2, 'noahking@example.com'),
('oliviawright', 'password17', 17, 2, 'oliviawright@example.com'),
('peterscott', 'password18', 18, 2, 'peterscott@example.com'),
('quinngreen', 'password19', 19, 2, 'quinngreen@example.com'),
('racheladams', 'password20', 20, 2, 'racheladams@example.com');


UPDATE exercise 
SET `name` = "Skocite"
WHERE id=27;

UPDATE additional_info 
SET `first_name` = "Vanko"
WHERE id=12;

DELETE FROM exercise WHERE id = 9;

DELETE FROM city WHERE id = 3;

/*Select*/


SELECT s.kg, e.name FROM sets s  
JOIN workout_details wd ON s.workout_details_id = wd.id
JOIN exercise e ON wd.exercise_id = e.id
WHERE e.name = "Skok";

SELECT u.username, r.role_name, w.workout_name, e.name, s.reps, s.kg, s.set_number,
mg.muscle_group_name FROM user u
JOIN `role` r ON u.role_id = r.id_role
JOIN workout w ON u.id_user = w.user_id
JOIN workout_details wd ON w.id_workout = wd.workout_id
JOIN exercise e ON wd.exercise_id = e.id
JOIN muscle_group mg ON wd.muscle_group_id = wd.muscle_group_id
JOIN sets s ON wd.id = s.workout_details_id WHERE u.id_user = 2;

SELECT u.username, r.role_name , ad.first_name, ad.last_name, c.city_name, ad.cm, ad.kg, ad.born_date, ad.gender
FROM user u 
JOIN role r ON u.role_id = r.id_role
JOIN additional_info ad ON  u.additional_info_id = ad.id
JOIN city c ON c.id = ad.city_id;

CREATE VIEW workout_with_tonnage AS
SELECT 
  w.id_workout,
  w.workout_name,
  w.date,
  u.username,
  SUM(s.reps * s.kg) AS total_tonnage_kg
FROM workout w
JOIN user u ON w.user_id = u.id_user
JOIN workout_details wd ON w.id_workout = wd.workout_id
JOIN sets s ON s.workout_details_id = wd.id
GROUP BY w.id_workout;

DELIMITER //

CREATE PROCEDURE get_workouts_by_muscle_group(IN input_muscle_group VARCHAR(45))
BEGIN
    SELECT 
        w.id_workout,
        w.workout_name,
        w.date,
        u.username,
        e.name AS exercise_name,
        mg.muscle_group_name
    FROM workout w
    JOIN user u ON w.user_id = u.id_user
    JOIN workout_details wd ON w.id_workout = wd.workout_id
    JOIN exercise e ON wd.exercise_id = e.id
    JOIN muscle_group mg ON wd.muscle_group_id = mg.id
    WHERE mg.muscle_group_name = input_muscle_group;
END //

DELIMITER ;

CREATE VIEW user_exercise_tonnage_view AS
SELECT 
    u.username,
    e.name AS exercise_name,
    SUM(s.reps * s.kg) AS total_tonnage_kg
FROM sets s
JOIN workout_details wd ON s.workout_details_id = wd.id
JOIN workout w ON wd.workout_id = w.id_workout
JOIN user u ON w.user_id = u.id_user
JOIN exercise e ON wd.exercise_id = e.id
GROUP BY u.username, e.name;

DELIMITER //

CREATE PROCEDURE search_workouts_by_user(
    IN userId INT,
    IN keyword VARCHAR(45)
)
BEGIN
    SELECT 
        w.id_workout,
        w.user_id,
        w.workout_name,
        w.date
    FROM 
        workout w
    WHERE 
        w.user_id = userId
        AND w.workout_name LIKE CONCAT('%', keyword, '%');
END //
DELIMITER ;

CREATE VIEW leader_board_view AS
SELECT 
    ROW_NUMBER() OVER (ORDER BY SUM(wwt.total_tonnage_kg) DESC) AS id, 
    u.username, 
    COUNT(w.id_workout) AS workout_counter, 
    SUM(wwt.total_tonnage_kg) AS sum_kg
FROM workout w
JOIN user u ON w.user_id = u.id_user,
     workout_with_tonnage wwt
WHERE w.user_id = u.id_user AND wwt.id_workout = w.id_workout
GROUP BY w.user_id, u.username;

DELIMITER $$
DROP FUNCTION IF EXISTS get_current_month_start$$

CREATE FUNCTION get_current_month_start()
RETURNS DATE
DETERMINISTIC
BEGIN
    RETURN DATE_FORMAT(CURDATE(), '%Y-%m-01');
END$$

DELIMITER ;

CREATE OR REPLACE VIEW user_workout_summary_view AS
SELECT 
    u.id_user,
    SUM(s.reps * s.kg) AS total_tonnage_kg,
    COUNT(DISTINCT w.id_workout) AS total_trainings,
    COALESCE(SUM(
        CASE 
            WHEN w.date >= get_current_month_start()
             AND w.date < DATE_ADD(get_current_month_start(), INTERVAL 1 MONTH)
            THEN s.reps * s.kg
            ELSE 0
        END
    ), 0) AS tonnage_this_month_kg,
    COUNT(DISTINCT 
        CASE
            WHEN w.date >= get_current_month_start()
             AND w.date < DATE_ADD(get_current_month_start(), INTERVAL 1 MONTH)
            THEN w.id_workout
        END
    ) AS trainings_this_month,
    (SELECT e2.name 
     FROM workout_details wd2
     JOIN workout w2 ON wd2.workout_id = w2.id_workout
     JOIN exercise e2 ON wd2.exercise_id = e2.id
     WHERE w2.user_id = u.id_user
     GROUP BY e2.id, e2.name
     ORDER BY COUNT(*) DESC
     LIMIT 1
    ) AS most_used_exercise,
    (SELECT COUNT(*)
     FROM workout_details wd2
     JOIN workout w2 ON wd2.workout_id = w2.id_workout
     WHERE w2.user_id = u.id_user
     GROUP BY wd2.exercise_id
     ORDER BY COUNT(*) DESC
     LIMIT 1
    ) AS most_used_exercise_count
FROM sets s
JOIN workout_details wd ON s.workout_details_id = wd.id
JOIN workout w ON wd.workout_id = w.id_workout
JOIN user u ON w.user_id = u.id_user 
GROUP BY u.id_user, u.username;