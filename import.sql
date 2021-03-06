-- MySQL Script generated by MySQL Workbench
-- Fri May 22 00:09:30 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema szakdolgozat
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `szakdolgozat` ;

-- -----------------------------------------------------
-- Schema szakdolgozat
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `szakdolgozat` DEFAULT CHARACTER SET utf8 COLLATE utf8_hungarian_ci ;
USE `szakdolgozat` ;

-- -----------------------------------------------------
-- Table `szakdolgozat`.`permission`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `szakdolgozat`.`permission` ;

CREATE TABLE IF NOT EXISTS `szakdolgozat`.`permission` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `szakdolgozat`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `szakdolgozat`.`user` ;

CREATE TABLE IF NOT EXISTS `szakdolgozat`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `login_name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `last_login` TIMESTAMP(1) NULL,
  `permission_id` INT NULL,
  `max_holiday` INT NULL,
  `can_log_in` TINYINT NULL,
  `deleted` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_permission1_idx` (`permission_id` ASC),
  CONSTRAINT `fk_user_permission1`
    FOREIGN KEY (`permission_id`)
    REFERENCES `szakdolgozat`.`permission` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '	';


-- -----------------------------------------------------
-- Table `szakdolgozat`.`work_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `szakdolgozat`.`work_group` ;

CREATE TABLE IF NOT EXISTS `szakdolgozat`.`work_group` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `scale` INT NOT NULL,
  `deleted` TINYINT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `szakdolgozat`.`activity_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `szakdolgozat`.`activity_group` ;

CREATE TABLE IF NOT EXISTS `szakdolgozat`.`activity_group` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `can_add_child` TINYINT NOT NULL,
  `deleted` TINYINT NOT NULL,
  `easy_log_in` TINYINT NOT NULL,
  `parent_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_activity_group_activity_group1_idx` (`parent_id` ASC),
  CONSTRAINT `fk_activity_group_activity_group1`
    FOREIGN KEY (`parent_id`)
    REFERENCES `szakdolgozat`.`activity_group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `szakdolgozat`.`activity`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `szakdolgozat`.`activity` ;

CREATE TABLE IF NOT EXISTS `szakdolgozat`.`activity` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(500) NULL,
  `activity_group_id` INT NULL,
  `is_task` TINYINT NOT NULL,
  `min` INT NULL,
  `deadline` DATETIME(1) NULL,
  `is_completed` TINYINT NULL,
  `date` DATE NOT NULL,
  `work_group_id` INT NULL,
  `owner_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_activity_activity_group1_idx` (`activity_group_id` ASC),
  INDEX `fk_activity_work_group1_idx` (`work_group_id` ASC),
  INDEX `fk_activity_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_activity_activity_group1`
    FOREIGN KEY (`activity_group_id`)
    REFERENCES `szakdolgozat`.`activity_group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_activity_work_group1`
    FOREIGN KEY (`work_group_id`)
    REFERENCES `szakdolgozat`.`work_group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_activity_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `szakdolgozat`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `szakdolgozat`.`user_work_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `szakdolgozat`.`user_work_group` ;

CREATE TABLE IF NOT EXISTS `szakdolgozat`.`user_work_group` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NULL,
  `work_group_id` INT NULL,
  `in_work_group_from` DATE NOT NULL,
  `in_work_group_to` DATE NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_has_work_group_work_group1_idx` (`work_group_id` ASC),
  INDEX `fk_user_has_work_group_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_user_has_work_group_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `szakdolgozat`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_work_group_work_group1`
    FOREIGN KEY (`work_group_id`)
    REFERENCES `szakdolgozat`.`work_group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `szakdolgozat`.`permission_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `szakdolgozat`.`permission_detail` ;

CREATE TABLE IF NOT EXISTS `szakdolgozat`.`permission_detail` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `role_tag` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `szakdolgozat`.`permission_details`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `szakdolgozat`.`permission_details` ;

CREATE TABLE IF NOT EXISTS `szakdolgozat`.`permission_details` (
  `permissions_id` INT NOT NULL,
  `details_id` INT NOT NULL,
  PRIMARY KEY (`permissions_id`, `details_id`),
  INDEX `fk_permission_has_permission_details_permission_details1_idx` (`details_id` ASC),
  INDEX `fk_permission_has_permission_details_permission1_idx` USING BTREE (`permissions_id`),
  CONSTRAINT `fk_permission_has_permission_details_permission1`
    FOREIGN KEY (`permissions_id`)
    REFERENCES `szakdolgozat`.`permission` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_permission_has_permission_details_permission_details1`
    FOREIGN KEY (`details_id`)
    REFERENCES `szakdolgozat`.`permission_detail` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `szakdolgozat`.`holiday`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `szakdolgozat`.`holiday` ;

CREATE TABLE IF NOT EXISTS `szakdolgozat`.`holiday` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `holiday_from` DATE NOT NULL,
  `holiday_to` DATE NOT NULL,
  `user_id` INT NOT NULL,
  `deleted` TINYINT NOT NULL,
  `days` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_holiday_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_holiday_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `szakdolgozat`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `szakdolgozat`.`work_group_activity_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `szakdolgozat`.`work_group_activity_group` ;

CREATE TABLE IF NOT EXISTS `szakdolgozat`.`work_group_activity_group` (
  `work_group_id` INT NOT NULL,
  `activity_group_id` INT NOT NULL,
  PRIMARY KEY (`work_group_id`, `activity_group_id`),
  INDEX `fk_work_group_has_activity_group_activity_group1_idx` (`activity_group_id` ASC),
  INDEX `fk_work_group_has_activity_group_work_group1_idx` (`work_group_id` ASC),
  CONSTRAINT `fk_work_group_has_activity_group_work_group1`
    FOREIGN KEY (`work_group_id`)
    REFERENCES `szakdolgozat`.`work_group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_work_group_has_activity_group_activity_group1`
    FOREIGN KEY (`activity_group_id`)
    REFERENCES `szakdolgozat`.`activity_group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `szakdolgozat`.`activity_plan`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `szakdolgozat`.`activity_plan` ;

CREATE TABLE IF NOT EXISTS `szakdolgozat`.`activity_plan` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATETIME(1) NOT NULL,
  `description` VARCHAR(500) NULL,
  `title` VARCHAR(45) NOT NULL,
  `user_id` INT NOT NULL,
  `owner_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_activity_plan_user1_idx` (`user_id` ASC),
  INDEX `fk_activity_plan_user2_idx` (`owner_id` ASC),
  CONSTRAINT `fk_activity_plan_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `szakdolgozat`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_activity_plan_user2`
    FOREIGN KEY (`owner_id`)
    REFERENCES `szakdolgozat`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO `PERMISSION_DETAIL` (`ID`, `NAME`, `ROLE_TAG`) VALUES (1, 'Feladat kiosztása', 'ROLE_ADD_TASK');
INSERT INTO `PERMISSION_DETAIL` (`ID`, `NAME`, `ROLE_TAG`) VALUES (2, 'Szabadságok kiosztása', 'ROLE_HOLIDAY_ADMIN');
INSERT INTO `PERMISSION_DETAIL` (`ID`, `NAME`, `ROLE_TAG`) VALUES (3, 'Felhasználók kezelése', 'ROLE_USER_ADMIN');
INSERT INTO `PERMISSION_DETAIL` (`ID`, `NAME`, `ROLE_TAG`) VALUES (4, 'Projektek kezelése', 'ROLE_PROJECT_ADMIN');
INSERT INTO `PERMISSION_DETAIL` (`ID`, `NAME`, `ROLE_TAG`) VALUES (5, 'Feladatok kezelése', 'ROLE_ACTIVITY_GROUP_ADMIN');
INSERT INTO `PERMISSION_DETAIL` (`ID`, `NAME`, `ROLE_TAG`) VALUES (6, 'Jogosultságok kezelése', 'ROLE_PERMISSION_ADMIN');
INSERT INTO `PERMISSION_DETAIL` (`ID`, `NAME`, `ROLE_TAG`) VALUES (7, 'Listázás', 'ROLE_LISTING');
INSERT INTO `PERMISSION_DETAIL` (`ID`, `NAME`, `ROLE_TAG`) VALUES (8, 'Időbeosztás tervezése', 'ROLE_ACTIVITY_PLAN_ADMIN');

INSERT INTO `PERMISSION` (`ID`, `NAME`) VALUES (1, 'Adminisztráció');

INSERT INTO `USER` (`ID`, `CAN_LOG_IN`, `EMAIL`, `FIRST_NAME`, `LAST_LOGIN`, `LAST_NAME`, `LOGIN_NAME`, `MAX_HOLIDAY`, `PASSWORD`, `PERMISSION_ID`, `DELETED`) VALUES (1, TRUE, 'k.roli9955@gmail.com', 'Roland', '2019-10-31T12:32:20', 'Kotroczó', 'admin', 20, '$2a$10$lW9Q6thFdccwPiRyOmQmreEtNhERoYGEsYKRL3uV97Q9u2ZGKxusW', 1, false);

INSERT INTO `PERMISSION_DETAILS` (`PERMISSIONS_ID`, `DETAILS_ID`) VALUES (1, 1);
INSERT INTO `PERMISSION_DETAILS` (`PERMISSIONS_ID`, `DETAILS_ID`) VALUES (1, 2);
INSERT INTO `PERMISSION_DETAILS` (`PERMISSIONS_ID`, `DETAILS_ID`) VALUES (1, 3);
INSERT INTO `PERMISSION_DETAILS` (`PERMISSIONS_ID`, `DETAILS_ID`) VALUES (1, 4);
INSERT INTO `PERMISSION_DETAILS` (`PERMISSIONS_ID`, `DETAILS_ID`) VALUES (1, 5);
INSERT INTO `PERMISSION_DETAILS` (`PERMISSIONS_ID`, `DETAILS_ID`) VALUES (1, 6);
INSERT INTO `PERMISSION_DETAILS` (`PERMISSIONS_ID`, `DETAILS_ID`) VALUES (1, 7);
INSERT INTO `PERMISSION_DETAILS` (`PERMISSIONS_ID`, `DETAILS_ID`) VALUES (1, 8);

INSERT INTO `ACTIVITY_GROUP` (`ID`, `CAN_ADD_CHILD`, `NAME`, `PARENT_ID`, `DELETED`, `EASY_LOG_IN`) VALUES (1, TRUE, 'Tervezés', NULL, FALSE, FALSE );
INSERT INTO `ACTIVITY_GROUP` (`ID`, `CAN_ADD_CHILD`, `NAME`, `PARENT_ID`, `DELETED`, `EASY_LOG_IN`) VALUES (2, TRUE, 'Adatbázis tervezés', 1, FALSE, FALSE);
INSERT INTO `ACTIVITY_GROUP` (`ID`, `CAN_ADD_CHILD`, `NAME`, `PARENT_ID`, `DELETED`, `EASY_LOG_IN`) VALUES (3, FALSE, 'UML Diagram készítés', 1, FALSE, FALSE);
INSERT INTO `ACTIVITY_GROUP` (`ID`, `CAN_ADD_CHILD`, `NAME`, `PARENT_ID`, `DELETED`, `EASY_LOG_IN`) VALUES (4, TRUE, 'Fejlesztés', NULL, FALSE, FALSE);
INSERT INTO `ACTIVITY_GROUP` (`ID`, `CAN_ADD_CHILD`, `NAME`, `PARENT_ID`, `DELETED`, `EASY_LOG_IN`) VALUES (5, FALSE, 'Frontend implementálás', 4, FALSE, FALSE);
INSERT INTO `ACTIVITY_GROUP` (`ID`, `CAN_ADD_CHILD`, `NAME`, `PARENT_ID`, `DELETED`, `EASY_LOG_IN`) VALUES (6, FALSE, 'Backend implementálás', 4, FALSE, FALSE);
INSERT INTO `ACTIVITY_GROUP` (`ID`, `CAN_ADD_CHILD`, `NAME`, `PARENT_ID`, `DELETED`, `EASY_LOG_IN`) VALUES (7, FALSE, 'Munka kezdése', NULL, FALSE, TRUE);
