-- Script INSERTs some starting data for the app

-- By default the password for the 'admin' account is admin
-- the starting salt for the admin account is "00000000000000000000000000000000"
-- which is obviously stored as a hexstring
INSERT INTO accounts (username, salt, password_and_salt_hash)
        VALUES ('admin', '3030303030303030303030303030303030303030303030303030303030303030',
        '893c750abe47d04ea480910a2bc8787d1323cd11d4e6eaa3a6c890dbeb42bfa0');
INSERT INTO admin_accounts (username) VALUES ('admin');

-- The genre for a movie can be any one of: Romance, Horror, Thriller, Comedy, Drama, Biopic, Action
INSERT INTO genres (genre_title) VALUES ('Romance');
INSERT INTO genres (genre_title) VALUES ('Horror');
INSERT INTO genres (genre_title) VALUES ('Thriller');
INSERT INTO genres (genre_title) VALUES ('Comedy');
INSERT INTO genres (genre_title) VALUES ('Drama');
INSERT INTO genres (genre_title) VALUES ('Biopic');
INSERT INTO genres (genre_title) VALUES ('Action');

-- Amenities should be selected out of a standard list of amenities
-- including ATMs, widescreen, snack bar, and restaurant
INSERT INTO amenities (amenity_type) VALUES ('ATM');
INSERT INTO amenities (amenity_type) VALUES ('Widescreen');
INSERT INTO amenities (amenity_type) VALUES ('Snack Bar');
INSERT INTO amenities (amenity_type) VALUES ('Restaurant');

-- Age-ratings [just selected from australian classfication board] (G, PG, M, MA15+, R18+ X18+) 
INSERT INTO age_ratings (rating) VALUES ('G'); 
INSERT INTO age_ratings (rating) VALUES ('PG'); 
INSERT INTO age_ratings (rating) VALUES ('M'); 
INSERT INTO age_ratings (rating) VALUES ('MA15+'); 
INSERT INTO age_ratings (rating) VALUES ('R18+'); 
INSERT INTO age_ratings (rating) VALUES ('X18+');

-- dummy data from here on out

-- just some coming soon movies
INSERT INTO movies (title, release_date, age_rating, director, actors, synopsis) 
    VALUES ('Ouija', '2014-10-24', 'PG', 'Stiles White', 'Olivia Cooke, Ana Coto, Daren Kagasoff',
    'A group of friends must confront their most terrifying fears when they awaken the dark powers of an ancient spirit board.');
INSERT INTO movies (title, release_date, age_rating, director, actors, synopsis) 
    VALUES ('White Bird in a Blizzard', '2014-10-25', 'PG', 'Gregg Araki', 'Shailene Woodley, Eva Green, Christopher Meloni',
    'In 1988, a teenage girl''s life is thrown into chaos when her mother disappears.');
INSERT INTO movies (title, release_date, age_rating, director, actors, synopsis) 
    VALUES ('John Wick', '2014-10-24', 'M', 'David Leitch, Chad Stahelski', 'Keanu Reeves, Bridget Moynahan, Willem Dafoe',
    'An ex-hitman comes out of retirement to track down the gangsters that took everything from him.');
INSERT INTO movies (title, release_date, age_rating, director, actors, synopsis) 
    VALUES ('Interstellar', '2014-11-07', 'PG', 'Christopher Nolan', 'Matthew McConaughey, Anne Hathaway, Jessica Chastain',
    'A group of explorers make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage.');
-- and some movies already out
INSERT INTO movies(title, release_date, age_rating, director, actors, synopsis) 
    VALUES('Shrek', '2001-05-18', 'G', 'Andrew Adamson, Vicky Jenson', 'Mike Myers, Eddie Murphy, Cameron Diaz',
    'An ogre, in order to regain his swamp, travels along with an annoying donkey in order to bring a princess to a scheming lord, wishing himself King.');
INSERT INTO movies(title, release_date, age_rating, director, actors, synopsis) 
    VALUES('Bob the Builder', '2001-01-15', 'PG', 'Keith Chapman', 'Rob Rackstraw, Kate Harbour, Neil Morrissey',
    'Bob the Builder is the animated adventures of Bob and his machines. Working together to overcome various challenges they get the job done.');
INSERT INTO movies(TITLE, RELEASE_DATE, age_rating, director, actors, synopsis) 
    VALUES('The Judge', '2014-10-10', 'MA15+', 'David Dobkin', 'Robert Downey Jr., Robert Duvall, Vera Farmiga',
    'Big city lawyer Hank Palmer returns to his childhood home where his father, the town''s judge, is suspected of murder. Hank sets out to discover the truth and, along the way, reconnects with his estranged family.');
INSERT INTO movies(title, release_date, age_rating, director, actors, synopsis) 
    VALUES('Gone Girl', '2014-10-03', 'MA15+', 'David Fincher', 'Ben Affleck, Rosamund Pike, Neil Patrick Harris',
    'With his wife''s disappearance having become the focus of an intense media circus, a man sees the spotlight turned on him when it''s suspected that he may not be innocent.');

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

--add some cinemas
INSERT INTO cinemas(location, capacity) VALUES ('Chatswood', 80);
INSERT INTO cinemas(location, capacity) VALUES ('Kingsford', 50);
INSERT INTO cinemas(location, capacity) VALUES ('City', 120);

--add amenities to cinemas
INSERT INTO cinemas_have_amenities(location, amenity) VALUES ('Chatswood', 'ATM');
INSERT INTO cinemas_have_amenities(location, amenity) VALUES ('Chatswood', 'Widescreen');
INSERT INTO cinemas_have_amenities(location, amenity) VALUES ('Chatswood', 'Snack Bar');
INSERT INTO cinemas_have_amenities(location, amenity) VALUES ('City', 'ATM');
INSERT INTO cinemas_have_amenities(location, amenity) VALUES ('City', 'Widescreen');
INSERT INTO cinemas_have_amenities(location, amenity) VALUES ('City', 'Snack Bar');
INSERT INTO cinemas_have_amenities(location, amenity) VALUES ('City', 'Restaurant');
INSERT INTO cinemas_have_amenities(location, amenity) VALUES ('Kingsford', 'ATM');
INSERT INTO cinemas_have_amenities(location, amenity) VALUES ('Kingsford', 'Snack Bar');

--add screenings
INSERT INTO movies_screen_in_cinemas(location, title, release_date, screening_time)
VALUES ('Chatswood', 'Ouija', '2014-10-24', '2014-10-24 00:00:00');
INSERT INTO movies_screen_in_cinemas(location, title, release_date, screening_time)
VALUES ('Kingsford', 'Ouija', '2014-10-24', '2014-10-24 00:00:00');
INSERT INTO movies_screen_in_cinemas(location, title, release_date, screening_time)
VALUES ('City', 'Ouija', '2014-10-24', '2014-10-24 00:00:00');
INSERT INTO movies_screen_in_cinemas(location, title, release_date, screening_time)
VALUES ('City', 'The Judge', '2014-10-10', '2014-10-12 10:00:00');
--TODO add more dummy screenings
