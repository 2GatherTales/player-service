-- -----------------------------------------------------
-- Schema avarum_game
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS avarum_game CASCADE;
DROP TABLE IF EXISTS avarum_game.player CASCADE;

CREATE SCHEMA avarum_game;
SHOW search_path;
SET search_path  TO avarum_game;


-- -----------------------------------------------------
-- Table avarum_game.player
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS avarum_game.player (
  id BIGSERIAL PRIMARY KEY NOT NULL,
    potion_chance SMALLINT NOT NULL,
    potion_quantity SMALLINT NOT NULL,
    potion_heal int[] NOT NULL,
    userid BIGINT NOT NULL,
    CONSTRAINT fk_userid FOREIGN KEY (userid) REFERENCES avarum_users.user (id));


-- -----------------------------------------------------
-- Add sample data
-- -----------------------------------------------------
INSERT INTO avarum_game.player (potion_chance, potion_quantity, potion_heal, userid)
VALUES (60, 1, '{30, 60}', 3);