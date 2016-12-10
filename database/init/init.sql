CREATE DATABASE IF NOT EXISTS barkeep;
USE barkeep;


CREATE TABLE lkpUsersBooks
(
  user INT(10) UNSIGNED    NOT NULL,
  book BIGINT(20) UNSIGNED NOT NULL
);


CREATE TABLE lkpUsersBars
(
  user INT(10) UNSIGNED    NOT NULL,
  bar  BIGINT(20) UNSIGNED NOT NULL
);


CREATE TABLE lkpBooksRecipes
(
  book   BIGINT(20) UNSIGNED NOT NULL,
  recipe BIGINT(20) UNSIGNED NOT NULL,
  number BIGINT(20) UNSIGNED NOT NULL,
  PRIMARY KEY (book, recipe)
);


CREATE TABLE lkpBarsIngredients
(
  bar        BIGINT(20) UNSIGNED NOT NULL,
  ingredient BIGINT(20) UNSIGNED NOT NULL
);


CREATE TABLE tblBars
(
  id          BIGINT(20) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
  user        INT(10) UNSIGNED                NOT NULL,
  title       VARCHAR(255)                    NOT NULL,
  description TEXT,
  createTime  TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP,
  modifyTime  TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE tblBooks
(
  id          BIGINT(20) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
  user        INT(10) UNSIGNED                NOT NULL,
  title       VARCHAR(255)                    NOT NULL,
  description TEXT,
  active      TINYINT(1) DEFAULT '1'          NOT NULL,
  createTime  TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP,
  modifyTime  TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE tblIngredients
(
  id         BIGINT(20) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
  title      VARCHAR(255)                    NOT NULL,
  parent     BIGINT(20) UNSIGNED,
  createTime TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP,
  modifyTime TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE tblRecipeComponents
(
  recipe       BIGINT(20) UNSIGNED  NOT NULL,
  ingredient   BIGINT(20) UNSIGNED  NOT NULL,
  min          DOUBLE UNSIGNED      NOT NULL,
  max          DOUBLE UNSIGNED,
  componentNum SMALLINT(5) UNSIGNED NOT NULL,
  `order`      SMALLINT(5) UNSIGNED NOT NULL
);


CREATE TABLE tblRecipes
(
  id           BIGINT(20) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
  title        VARCHAR(255)                    NOT NULL,
  description  TEXT,
  imageUrl     TEXT,
  instructions TEXT,
  source       TEXT,
  createTime   TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP,
  modifyTime   TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE tblUsers
(
  id          INT(10) UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username    VARCHAR(255)                 NOT NULL,
  password    VARCHAR(255)                 NOT NULL,
  displayName VARCHAR(255)                 NOT NULL,
  email       VARCHAR(255)                 NOT NULL,
  createTime  TIMESTAMP                             DEFAULT CURRENT_TIMESTAMP,
  modifyTime  TIMESTAMP                             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


ALTER TABLE lkpUsersBooks
  ADD FOREIGN KEY (user) REFERENCES tblUsers (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE lkpUsersBooks
  ADD FOREIGN KEY (book) REFERENCES tblBooks (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
CREATE INDEX FK_lkpUsersBooks_tblUsers
  ON lkpUsersBooks (user);
CREATE INDEX FK_lkpUsersBooks_tblBooks
  ON lkpUsersBooks (book);


ALTER TABLE lkpUsersBars
  ADD FOREIGN KEY (user) REFERENCES tblUsers (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE lkpUsersBars
  ADD FOREIGN KEY (bar) REFERENCES tblBars (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
CREATE INDEX FK_lkpUsersBars_tblUsers
  ON lkpUsersBars (user);
CREATE INDEX FK_lkpUsersBars_tblBars
  ON lkpUsersBars (bar);


ALTER TABLE lkpBooksRecipes
  ADD FOREIGN KEY (book) REFERENCES tblBooks (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE lkpBooksRecipes
  ADD FOREIGN KEY (recipe) REFERENCES tblRecipes (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
CREATE INDEX FK_lkpBooksRecipes_tblBooks
  ON lkpBooksRecipes (book);
CREATE INDEX FK_lkpBooksRecipes_tblRecipes
  ON lkpBooksRecipes (number);


ALTER TABLE lkpBarsIngredients
  ADD FOREIGN KEY (bar) REFERENCES tblBars (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE lkpBarsIngredients
  ADD FOREIGN KEY (ingredient) REFERENCES tblIngredients (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
CREATE INDEX FK_lkpBarsIngredients_tblBars
  ON lkpBarsIngredients (bar);
CREATE INDEX FK_lkpBarsIngredients_tblIngredients
  ON lkpBarsIngredients (ingredient);


ALTER TABLE tblBars
  ADD FOREIGN KEY (user) REFERENCES tblUsers (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
CREATE INDEX FK_tblBars_tblUsers
  ON tblBars (user);


ALTER TABLE tblBooks
  ADD FOREIGN KEY (user) REFERENCES tblUsers (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
CREATE INDEX FK_tblBooks_tblUsers
  ON tblBooks (user);


ALTER TABLE tblIngredients
  ADD FOREIGN KEY (parent) REFERENCES tblIngredients (id)
  ON UPDATE CASCADE;
CREATE INDEX FK_tblIngredients_tblIngredients
  ON tblIngredients (parent);


ALTER TABLE tblRecipeComponents
  ADD FOREIGN KEY (recipe) REFERENCES tblRecipes (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE tblRecipeComponents
  ADD FOREIGN KEY (ingredient) REFERENCES tblIngredients (id)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
CREATE INDEX FK_tblRecipeComponents_tblIngredients
  ON tblRecipeComponents (ingredient);
CREATE INDEX FK_tblRecipeComponents_tblRecipes
  ON tblRecipeComponents (recipe);


CREATE UNIQUE INDEX tblUsers_email_uindex
  ON tblUsers (email);
CREATE UNIQUE INDEX tblUsers_id_uindex
  ON tblUsers (id);
