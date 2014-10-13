-- Script inserts some starting data for the app

-- By default the password for the 'admin' account is admin;
-- the starting salt for the admin account is "00000000000000000000000000000000"
-- which is obviously stored as a hexstring
INSERT INTO account (username, salt, password_and_salt_hash)
        values ('admin', '3030303030303030303030303030303030303030303030303030303030303030',
        '893c750abe47d04ea480910a2bc8787d1323cd11d4e6eaa3a6c890dbeb42bfa0');
INSERT INTO admin_account (username) values ('admin');

-- The genre for a movie can be any one of: Romance, Horror, Thriller, Comedy, Drama, Biopic, Action
INSERT INTO genre (genre_title) VALUES ('Romance');
INSERT INTO genre (genre_title) VALUES ('Horror');
INSERT INTO genre (genre_title) VALUES ('Thriller');
INSERT INTO genre (genre_title) VALUES ('Comedy');
INSERT INTO genre (genre_title) VALUES ('Drama');
INSERT INTO genre (genre_title) VALUES ('Biopic');
INSERT INTO genre (genre_title) VALUES ('Action');

-- Amenities should be selected out of a standard list of amenities
-- including ATMs, widescreen, snack bar, and restaurant
INSERT INTO AMENITY (AMENITY_TYPE) values ('ATM');
INSERT INTO AMENITY (AMENITY_TYPE) values ('Widescreen');
INSERT INTO AMENITY (AMENITY_TYPE) values ('Snack Bar');
INSERT INTO AMENITY (AMENITY_TYPE) values ('Restaurant');

-- Age-ratings [just selected from australian classfication board] (G, PG, M, MA15+, R18+ X18+) 
insert into AGE_RATING (RATING) values ('G'); 
insert into AGE_RATING (RATING) values ('PG'); 
insert into AGE_RATING (RATING) values ('M'); 
insert into AGE_RATING (RATING) values ('MA15+'); 
insert into AGE_RATING (RATING) values ('R18+'); 
insert into AGE_RATING (RATING) values ('X18+');

-- dummy data from here on out

-- just some coming soon movies
insert into MOVIE (TITLE, RELEASE_DATE) values ('Ouija', '2014-10-24');
insert into MOVIE (TITLE, RELEASE_DATE) values ('White Bird in a Blizzard', '2014-10-25');
insert into MOVIE (TITLE, RELEASE_DATE) values ('John Wick', '2014-10-24');
insert into MOVIE (TITLE, RELEASE_DATE) values ('Interstellar', '2014-11-07');
-- and some movies already out
INSERT INTO movie(title, release_date) VALUES('Shrek', '2001-05-18');
INSERT INTO movie(title, release_date) VALUES('Bob the Builder', '2001-01-15');
insert into MOVIE(TITLE, RELEASE_DATE) values('The Judge', '2014-10-10');
insert into movie(title, release_date) values('Gone Girl', '2014-10-03');

-- add genres to these movies
INSERT INTO movies_have_genres(title, release_date, genre_title)
VALUES('Ouija', '2014-10-24', 'Horror');
INSERT INTO movies_have_genres(title, release_date, genre_title)
VALUES('White Bird in a Blizzard', '2014-10-25', 'Drama');
INSERT INTO movies_have_genres(title, release_date, genre_title)
VALUES('White Bird in a Blizzard', '2014-10-25', 'Thriller');
INSERT INTO movies_have_genres(title, release_date, genre_title)
VALUES('John Wick', '2014-10-24', 'Action');
INSERT INTO movies_have_genres(title, release_date, genre_title)
VALUES('John Wick', '2014-10-24', 'Thriller');
INSERT INTO movies_have_genres(title, release_date, genre_title)
VALUES('Interstellar', '2014-11-07', 'Thriller');
INSERT INTO movies_have_genres(title, release_date, genre_title)
VALUES('Shrek', '2001-05-18', 'Comedy');
INSERT INTO movies_have_genres(title, release_date, genre_title)
VALUES('The Judge', '2014-10-10', 'Drama');
INSERT INTO movies_have_genres(title, release_date, genre_title)
VALUES('Gone Girl', '2014-10-03', 'Drama');
INSERT INTO movies_have_genres(title, release_date, genre_title)
VALUES('Gone Girl', '2014-10-03', 'Thriller');

--add age ratings to these movies
insert into MOVIES_HAVE_AGE_RATINGS(TITLE, RELEASE_DATE, RATING)
values ('Ouija', '2014-10-24', 'PG');
insert into MOVIES_HAVE_AGE_RATINGS(TITLE, RELEASE_DATE, RATING)
values ('White Bird in a Blizzard', '2014-10-25', 'PG');
insert into MOVIES_HAVE_AGE_RATINGS(TITLE, RELEASE_DATE, RATING)
values ('John Wick', '2014-10-24', 'M');
insert into MOVIES_HAVE_AGE_RATINGS(TITLE, RELEASE_DATE, RATING)
values ('Interstellar', '2014-11-07', 'PG');
insert into MOVIES_HAVE_AGE_RATINGS(TITLE, RELEASE_DATE, RATING)
values ('Shrek', '2001-05-18', 'G');
insert into MOVIES_HAVE_AGE_RATINGS(TITLE, RELEASE_DATE, RATING)
values ('The Judge', '2014-10-10', 'MA15+');
insert into MOVIES_HAVE_AGE_RATINGS(TITLE, RELEASE_DATE, RATING)
values ('Gone Girl', '2014-10-03', 'MA15+');
insert into MOVIES_HAVE_AGE_RATINGS(TITLE, RELEASE_DATE, RATING)
values ('Bob the Builder', '2001-01-15', 'PG');

--add some cinemas
insert into CINEMA(location, capacity) values ('Chatswood', 80);
insert into CINEMA(location, capacity) values ('Kingsford', 50);
insert into CINEMA(location, capacity) values ('City', 120);

--add amenities to cinemas
insert into CINEMAS_HAVE_AMENITIES(location, amenity) values ('Chatswood', 'ATM');
insert into CINEMAS_HAVE_AMENITIES(location, amenity) values ('Chatswood', 'Widescreen');
insert into CINEMAS_HAVE_AMENITIES(location, amenity) values ('Chatswood', 'Snack Bar');
insert into CINEMAS_HAVE_AMENITIES(location, amenity) values ('City', 'ATM');
insert into CINEMAS_HAVE_AMENITIES(location, amenity) values ('City', 'Widescreen');
insert into CINEMAS_HAVE_AMENITIES(location, amenity) values ('City', 'Snack Bar');
insert into CINEMAS_HAVE_AMENITIES(location, amenity) values ('City', 'Restaurant');
insert into CINEMAS_HAVE_AMENITIES(location, amenity) values ('Kingsford', 'ATM');
insert into CINEMAS_HAVE_AMENITIES(location, amenity) values ('Kingsford', 'Snack Bar');

--add screenings
insert into MOVIES_SCREEN_IN_CINEMAS(location, TITLE, RELEASE_DATE, SCREENING_TIME)
values ('Chatswood', 'Ouija', '2014-10-24', '2014-10-24 00:00:00');
insert into MOVIES_SCREEN_IN_CINEMAS(location, TITLE, RELEASE_DATE, SCREENING_TIME)
values ('Kingsford', 'Ouija', '2014-10-24', '2014-10-24 00:00:00');
insert into MOVIES_SCREEN_IN_CINEMAS(location, TITLE, RELEASE_DATE, SCREENING_TIME)
values ('City', 'Ouija', '2014-10-24', '2014-10-24 00:00:00');
insert into MOVIES_SCREEN_IN_CINEMAS(location, TITLE, RELEASE_DATE, SCREENING_TIME)
values ('City', 'The Judge', '2014-10-10', '2014-10-12 10:00:00');
--TODO add more dummy screenings
