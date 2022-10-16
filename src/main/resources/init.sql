CREATE SCHEMA IF NOT EXISTS test;
USE test;

--DROP TABLE IF EXISTS players;
CREATE TABLE players
(
    Id int PRIMARY KEY,
    Nickname varchar(30) NOT NULL
);

--DROP TABLE IF EXISTS progresses;
CREATE TABLE progresses
(
    Id int PRIMARY KEY,
    PlayerId int REFERENCES players(Id),
    ResourceId int NOT NULL,
    Score int NOT NULL,
    MaxScore int NOT NULL
);

--DROP TABLE IF EXISTS currencies;
CREATE TABLE currencies
(
    Id int PRIMARY KEY,
    PlayerId int REFERENCES players(Id),
    ResourceId int NOT NULL,
    Name VARCHAR(30) NOT NULL,
    Count int NOT NULL
);

--DROP TABLE IF EXISTS items;
CREATE TABLE items
(
    Id int PRIMARY KEY,
    PlayerId int REFERENCES players(Id),
    ResourceId int NOT NULL,
    Count int NOT NULL,
    Level int NOT NULL
);