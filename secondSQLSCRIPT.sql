-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema strong_beton
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema strong_beton
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `strong_beton` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `strong_beton` ;

-- -----------------------------------------------------
-- Table `strong_beton`.`city`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`city` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `city_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 47
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `strong_beton`.`additional_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`additional_info` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `city_id` INT NOT NULL,
  `street_info` VARCHAR(65) NOT NULL,
  `cm` INT NULL DEFAULT NULL,
  `kg` FLOAT(7,2) NULL DEFAULT NULL,
  `born_date` DATE NULL DEFAULT NULL,
  `gender` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `additional_city_fk` (`city_id` ASC) VISIBLE,
  CONSTRAINT `additional_city_fk`
    FOREIGN KEY (`city_id`)
    REFERENCES `strong_beton`.`city` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 46
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `strong_beton`.`exercise`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`exercise` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 53
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `strong_beton`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`role` (
  `id_role` INT NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_role`),
  UNIQUE INDEX `role_name_UNIQUE` (`role_name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `strong_beton`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`user` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` CHAR(80) NOT NULL,
  `additional_info_id` INT NULL DEFAULT NULL,
  `role_id` INT NOT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `email` (`email` ASC) VISIBLE,
  UNIQUE INDEX `additional_info_id_UNIQUE` (`additional_info_id` ASC) VISIBLE,
  INDEX `user_role_fk` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_additional_info`
    FOREIGN KEY (`additional_info_id`)
    REFERENCES `strong_beton`.`additional_info` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `user_role_fk`
    FOREIGN KEY (`role_id`)
    REFERENCES `strong_beton`.`role` (`id_role`))
ENGINE = InnoDB
AUTO_INCREMENT = 43
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `strong_beton`.`friendship`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`friendship` (
  `id_friendship` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `friend_id` INT NOT NULL,
  `status` ENUM('Pending', 'Nothing', 'Accepted', 'Response') NOT NULL,
  PRIMARY KEY (`id_friendship`),
  UNIQUE INDEX `uniq_friendship` (`user_id` ASC, `friend_id` ASC) VISIBLE,
  INDEX `friend_id` (`friend_id` ASC) VISIBLE,
  CONSTRAINT `friendship_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `strong_beton`.`user` (`id_user`),
  CONSTRAINT `friendship_ibfk_2`
    FOREIGN KEY (`friend_id`)
    REFERENCES `strong_beton`.`user` (`id_user`))
ENGINE = InnoDB
AUTO_INCREMENT = 107
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `strong_beton`.`muscle_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`muscle_group` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `muscle_group_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `muscle_group_name_UNIQUE` (`muscle_group_name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 117
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `strong_beton`.`workout_template`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`workout_template` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `workout_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 31
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `strong_beton`.`workout`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`workout` (
  `id_workout` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `workout_template_id` INT NOT NULL,
  PRIMARY KEY (`id_workout`),
  INDEX `workout_user_fk` (`user_id` ASC) VISIBLE,
  INDEX `workout_template_id` (`workout_template_id` ASC) VISIBLE,
  CONSTRAINT `workout_ibfk_1`
    FOREIGN KEY (`workout_template_id`)
    REFERENCES `strong_beton`.`workout_template` (`id`),
  CONSTRAINT `workout_user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `strong_beton`.`user` (`id_user`))
ENGINE = InnoDB
AUTO_INCREMENT = 73
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `strong_beton`.`workout_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`workout_details` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `workout_id` INT NOT NULL,
  `exercise_id` INT NOT NULL,
  `muscle_group_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `workout_details_workout_fk` (`workout_id` ASC) VISIBLE,
  INDEX `workout_details_exercise_idx` (`exercise_id` ASC) VISIBLE,
  INDEX `fk_workoutDetails_muscleGroup` (`muscle_group_id` ASC) VISIBLE,
  CONSTRAINT `fk_workoutDetails_exercise`
    FOREIGN KEY (`exercise_id`)
    REFERENCES `strong_beton`.`exercise` (`id`),
  CONSTRAINT `fk_workoutDetails_muscleGroup`
    FOREIGN KEY (`muscle_group_id`)
    REFERENCES `strong_beton`.`muscle_group` (`id`),
  CONSTRAINT `workout_details_workout_fk`
    FOREIGN KEY (`workout_id`)
    REFERENCES `strong_beton`.`workout` (`id_workout`))
ENGINE = InnoDB
AUTO_INCREMENT = 105
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `strong_beton`.`sets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`sets` (
  `id_sets` INT NOT NULL AUTO_INCREMENT,
  `reps` INT NOT NULL,
  `kg` FLOAT(10,2) NOT NULL,
  `set_number` INT NOT NULL,
  `workout_details_id` INT NOT NULL,
  PRIMARY KEY (`id_sets`),
  INDEX `stats_workout_details` (`workout_details_id` ASC) VISIBLE,
  CONSTRAINT `stats_workout_details`
    FOREIGN KEY (`workout_details_id`)
    REFERENCES `strong_beton`.`workout_details` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 222
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

USE `strong_beton` ;

-- -----------------------------------------------------
-- Placeholder table for view `strong_beton`.`leader_board_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`leader_board_view` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `strong_beton`.`show_friend_list_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`show_friend_list_view` (`id` INT);

-- -----------------------------------------------------
-- Placeholder table for view `strong_beton`.`user_exercise_tonnage_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`user_exercise_tonnage_view` (`username` INT, `exercise_name` INT, `total_tonnage_kg` INT);

-- -----------------------------------------------------
-- Placeholder table for view `strong_beton`.`user_workout_summary_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`user_workout_summary_view` (`id_user` INT, `total_tonnage_kg` INT, `total_trainings` INT, `tonnage_this_month_kg` INT, `trainings_this_month` INT, `most_used_exercise` INT, `most_used_exercise_count` INT);

-- -----------------------------------------------------
-- Placeholder table for view `strong_beton`.`workout_with_tonnage`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `strong_beton`.`workout_with_tonnage` (`id_workout` INT, `workout_name` INT, `date` INT, `username` INT, `total_tonnage_kg` INT);

-- -----------------------------------------------------
-- function get_current_month_start
-- -----------------------------------------------------

DELIMITER $$
USE `strong_beton`$$
CREATE DEFINER=`root`@`localhost` FUNCTION `get_current_month_start`() RETURNS date
    DETERMINISTIC
BEGIN
    RETURN DATE_FORMAT(CURDATE(), '%Y-%m-01');
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure get_workouts_by_muscle_group
-- -----------------------------------------------------

DELIMITER $$
USE `strong_beton`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_workouts_by_muscle_group`(IN input_muscle_group VARCHAR(45))
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
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure search_workouts_by_user
-- -----------------------------------------------------

DELIMITER $$
USE `strong_beton`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_workouts_by_user`(
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
END$$

DELIMITER ;

-- -----------------------------------------------------
-- View `strong_beton`.`leader_board_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `strong_beton`.`leader_board_view`;
USE `strong_beton`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `strong_beton`.`leader_board_view` AS select row_number() OVER (ORDER BY sum(`strong_beton`.`wwt`.`total_tonnage_kg`) desc )  AS `id`,`u`.`username` AS `username`,count(`w`.`id_workout`) AS `workout_counter`,sum(`strong_beton`.`wwt`.`total_tonnage_kg`) AS `sum_kg` from ((`strong_beton`.`workout` `w` join `strong_beton`.`user` `u` on((`w`.`user_id` = `u`.`id_user`))) join `strong_beton`.`workout_with_tonnage` `wwt`) where ((`w`.`user_id` = `u`.`id_user`) and (`strong_beton`.`wwt`.`id_workout` = `w`.`id_workout`)) group by `w`.`user_id`,`u`.`username`;

-- -----------------------------------------------------
-- View `strong_beton`.`show_friend_list_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `strong_beton`.`show_friend_list_view`;
USE `strong_beton`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `strong_beton`.`show_friend_list_view` AS select row_number() OVER (ORDER BY `u`.`username` )  AS `id`,`u`.`username` AS `username`,`u2`.`username` AS `friend`,`f`.`status` AS `status` from ((`strong_beton`.`friendship` `f` join `strong_beton`.`user` `u` on((`f`.`user_id` = `u`.`id_user`))) join `strong_beton`.`user` `u2` on((`f`.`friend_id` = `u2`.`id_user`)));

-- -----------------------------------------------------
-- View `strong_beton`.`user_exercise_tonnage_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `strong_beton`.`user_exercise_tonnage_view`;
USE `strong_beton`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `strong_beton`.`user_exercise_tonnage_view` AS select `u`.`username` AS `username`,`e`.`name` AS `exercise_name`,sum((`s`.`reps` * `s`.`kg`)) AS `total_tonnage_kg` from ((((`strong_beton`.`sets` `s` join `strong_beton`.`workout_details` `wd` on((`s`.`workout_details_id` = `wd`.`id`))) join `strong_beton`.`workout` `w` on((`wd`.`workout_id` = `w`.`id_workout`))) join `strong_beton`.`user` `u` on((`w`.`user_id` = `u`.`id_user`))) join `strong_beton`.`exercise` `e` on((`wd`.`exercise_id` = `e`.`id`))) group by `u`.`username`,`e`.`name`;

-- -----------------------------------------------------
-- View `strong_beton`.`user_workout_summary_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `strong_beton`.`user_workout_summary_view`;
USE `strong_beton`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `strong_beton`.`user_workout_summary_view` AS select `u`.`id_user` AS `id_user`,sum((`s`.`reps` * `s`.`kg`)) AS `total_tonnage_kg`,count(distinct `w`.`id_workout`) AS `total_trainings`,coalesce(sum((case when ((`w`.`date` >= `get_current_month_start`()) and (`w`.`date` < (`get_current_month_start`() + interval 1 month))) then (`s`.`reps` * `s`.`kg`) else 0 end)),0) AS `tonnage_this_month_kg`,count(distinct (case when ((`w`.`date` >= `get_current_month_start`()) and (`w`.`date` < (`get_current_month_start`() + interval 1 month))) then `w`.`id_workout` end)) AS `trainings_this_month`,(select `e2`.`name` from ((`strong_beton`.`workout_details` `wd2` join `strong_beton`.`workout` `w2` on((`wd2`.`workout_id` = `w2`.`id_workout`))) join `strong_beton`.`exercise` `e2` on((`wd2`.`exercise_id` = `e2`.`id`))) where (`w2`.`user_id` = `u`.`id_user`) group by `e2`.`id`,`e2`.`name` order by count(0) desc limit 1) AS `most_used_exercise`,(select count(0) from (`strong_beton`.`workout_details` `wd2` join `strong_beton`.`workout` `w2` on((`wd2`.`workout_id` = `w2`.`id_workout`))) where (`w2`.`user_id` = `u`.`id_user`) group by `wd2`.`exercise_id` order by count(0) desc limit 1) AS `most_used_exercise_count` from (((`strong_beton`.`sets` `s` join `strong_beton`.`workout_details` `wd` on((`s`.`workout_details_id` = `wd`.`id`))) join `strong_beton`.`workout` `w` on((`wd`.`workout_id` = `w`.`id_workout`))) join `strong_beton`.`user` `u` on((`w`.`user_id` = `u`.`id_user`))) group by `u`.`id_user`,`u`.`username`;

-- -----------------------------------------------------
-- View `strong_beton`.`workout_with_tonnage`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `strong_beton`.`workout_with_tonnage`;
USE `strong_beton`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `strong_beton`.`workout_with_tonnage` AS select `w`.`id_workout` AS `id_workout`,`wt`.`workout_name` AS `workout_name`,`w`.`date` AS `date`,`u`.`username` AS `username`,sum((`s`.`reps` * `s`.`kg`)) AS `total_tonnage_kg` from ((((`strong_beton`.`workout` `w` join `strong_beton`.`user` `u` on((`w`.`user_id` = `u`.`id_user`))) join `strong_beton`.`workout_details` `wd` on((`w`.`id_workout` = `wd`.`workout_id`))) join `strong_beton`.`sets` `s` on((`s`.`workout_details_id` = `wd`.`id`))) join `strong_beton`.`workout_template` `wt` on((`w`.`workout_template_id` = `wt`.`id`))) group by `w`.`id_workout`;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
