-- StrongBeton Railway-safe database views
-- Import into the `strong_beton` database.
-- This file intentionally removes MySQL Workbench DEFINER=`root`@`localhost`
-- so it works on Railway MySQL.

USE `strong_beton`;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE;
SET SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- Drop existing views first
DROP VIEW IF EXISTS `leader_board_view`;
DROP VIEW IF EXISTS `show_friend_list_view`;
DROP VIEW IF EXISTS `user_exercise_tonnage_view`;
DROP VIEW IF EXISTS `user_workout_summary_view`;
DROP VIEW IF EXISTS `workout_with_tonnage`;

-- Drop placeholder tables if they were created by MySQL Workbench forward-engineering scripts
DROP TABLE IF EXISTS `leader_board_view`;
DROP TABLE IF EXISTS `show_friend_list_view`;
DROP TABLE IF EXISTS `user_exercise_tonnage_view`;
DROP TABLE IF EXISTS `user_workout_summary_view`;
DROP TABLE IF EXISTS `workout_with_tonnage`;

-- -----------------------------------------------------
-- View `workout_with_tonnage`
-- -----------------------------------------------------
CREATE OR REPLACE
ALGORITHM=UNDEFINED
SQL SECURITY INVOKER
VIEW `workout_with_tonnage` AS
SELECT
    `w`.`uuid_workout` AS `id_workout`,
    `wt`.`workout_name` AS `workout_name`,
    `w`.`date` AS `date`,
    `u`.`username` AS `username`,
    SUM((`s`.`reps` * `s`.`kg`)) AS `total_tonnage_kg`
FROM ((((`workout` `w`
    JOIN `user` `u` ON ((`w`.`user_id` = `u`.`id`)))
    JOIN `workout_details` `wd` ON ((`w`.`uuid_workout` = `wd`.`workout_uuid`)))
    JOIN `sets` `s` ON ((`s`.`workout_details_id` = `wd`.`id`)))
    JOIN `workout_template` `wt` ON ((`w`.`workout_template_id` = `wt`.`id`)))
GROUP BY
    `w`.`uuid_workout`,
    `wt`.`workout_name`,
    `w`.`date`,
    `u`.`username`;

-- -----------------------------------------------------
-- View `leader_board_view`
-- -----------------------------------------------------
CREATE OR REPLACE
ALGORITHM=UNDEFINED
SQL SECURITY INVOKER
VIEW `leader_board_view` AS
SELECT
    ROW_NUMBER() OVER (ORDER BY `t`.`sum_kg` DESC) AS `id`,
    `t`.`username` AS `username`,
    `t`.`workout_counter` AS `workout_counter`,
    `t`.`sum_kg` AS `sum_kg`
FROM (
    SELECT
        `u`.`username` AS `username`,
        COUNT(`w`.`uuid_workout`) AS `workout_counter`,
        SUM(`wwt`.`total_tonnage_kg`) AS `sum_kg`
    FROM ((`workout` `w`
        JOIN `user` `u` ON ((`w`.`user_id` = `u`.`id`)))
        JOIN `workout_with_tonnage` `wwt` ON ((`wwt`.`id_workout` = `w`.`uuid_workout`)))
    GROUP BY
        `w`.`user_id`,
        `u`.`username`
) `t`;

-- -----------------------------------------------------
-- View `show_friend_list_view`
-- -----------------------------------------------------
CREATE OR REPLACE
ALGORITHM=UNDEFINED
SQL SECURITY INVOKER
VIEW `show_friend_list_view` AS
SELECT
    ROW_NUMBER() OVER (ORDER BY `u`.`username`) AS `id`,
    `u`.`username` AS `username`,
    `u2`.`username` AS `friend`,
    `f`.`status` AS `status`
FROM ((`friendship` `f`
    JOIN `user` `u` ON ((`f`.`user_id` = `u`.`id`)))
    JOIN `user` `u2` ON ((`f`.`friend_id` = `u2`.`id`)));

-- -----------------------------------------------------
-- View `user_exercise_tonnage_view`
-- -----------------------------------------------------
CREATE OR REPLACE
ALGORITHM=UNDEFINED
SQL SECURITY INVOKER
VIEW `user_exercise_tonnage_view` AS
SELECT
    `u`.`username` AS `username`,
    `e`.`name` AS `exercise_name`,
    SUM((`s`.`reps` * `s`.`kg`)) AS `total_tonnage_kg`
FROM ((((`sets` `s`
    JOIN `workout_details` `wd` ON ((`s`.`workout_details_id` = `wd`.`id`)))
    JOIN `workout` `w` ON ((`wd`.`workout_uuid` = `w`.`uuid_workout`)))
    JOIN `user` `u` ON ((`w`.`user_id` = `u`.`id`)))
    JOIN `exercise` `e` ON ((`wd`.`exercise_id` = `e`.`id`)))
GROUP BY
    `u`.`username`,
    `e`.`name`;

-- -----------------------------------------------------
-- View `user_workout_summary_view`
-- -----------------------------------------------------
CREATE OR REPLACE
ALGORITHM=UNDEFINED
SQL SECURITY INVOKER
VIEW `user_workout_summary_view` AS
SELECT
    `u`.`id` AS `id_user`,
    SUM((`s`.`reps` * `s`.`kg`)) AS `total_tonnage_kg`,
    COUNT(DISTINCT `w`.`uuid_workout`) AS `total_trainings`,
    COALESCE(SUM((CASE
        WHEN ((`w`.`date` >= DATE_FORMAT(CURDATE(), '%Y-%m-01'))
          AND (`w`.`date` < DATE_FORMAT((CURDATE() + INTERVAL 1 MONTH), '%Y-%m-01')))
        THEN (`s`.`reps` * `s`.`kg`)
        ELSE 0
    END)), 0) AS `tonnage_this_month_kg`,
    COUNT(DISTINCT (CASE
        WHEN ((`w`.`date` >= DATE_FORMAT(CURDATE(), '%Y-%m-01'))
          AND (`w`.`date` < DATE_FORMAT((CURDATE() + INTERVAL 1 MONTH), '%Y-%m-01')))
        THEN `w`.`uuid_workout`
    END)) AS `trainings_this_month`,
    (
        SELECT `e2`.`name`
        FROM ((`workout_details` `wd2`
            JOIN `workout` `w2` ON ((`wd2`.`workout_uuid` = `w2`.`uuid_workout`)))
            JOIN `exercise` `e2` ON ((`wd2`.`exercise_id` = `e2`.`id`)))
        WHERE (`w2`.`user_id` = `u`.`id`)
        GROUP BY `e2`.`id`, `e2`.`name`
        ORDER BY COUNT(0) DESC
        LIMIT 1
    ) AS `most_used_exercise`,
    (
        SELECT COUNT(0)
        FROM (`workout_details` `wd2`
            JOIN `workout` `w2` ON ((`wd2`.`workout_uuid` = `w2`.`uuid_workout`)))
        WHERE (`w2`.`user_id` = `u`.`id`)
        GROUP BY `wd2`.`exercise_id`
        ORDER BY COUNT(0) DESC
        LIMIT 1
    ) AS `most_used_exercise_count`
FROM (((`sets` `s`
    JOIN `workout_details` `wd` ON ((`s`.`workout_details_id` = `wd`.`id`)))
    JOIN `workout` `w` ON ((`wd`.`workout_uuid` = `w`.`uuid_workout`)))
    JOIN `user` `u` ON ((`w`.`user_id` = `u`.`id`)))
GROUP BY `u`.`id`;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
