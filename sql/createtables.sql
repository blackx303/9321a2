DROP TABLE viewer_accounts_book_screenings;
DROP TABLE movies_screen_in_cinemas;
DROP TABLE cinemas_have_amenities;
DROP TABLE movies_have_age_ratings;
DROP TABLE directors_direct_movies;
DROP TABLE actors_act_in_movies;
DROP TABLE movies_have_genres;
DROP TABLE viewers_rate_movies;
DROP TABLE cinema;
DROP TABLE amenity;
DROP TABLE film_professional;
DROP TABLE age_rating;
DROP TABLE genre;
DROP TABLE viewer_account;
DROP TABLE admin_account;
DROP TABLE account;
DROP TABLE movie;

CREATE TABLE movie (
    title varchar(255) not null,
    release_date date not null,
    poster blob,
    
    primary key (title, release_date)
);

CREATE TABLE account (
    username varchar(16) not null,
    salt char(64) not null, --salt is 32 bytes long, represented as a 64char hexstring
    password_and_salt_hash char(64) not null, --we use sha256(concat(salt, password))
                                              --salt is changed on password change
                                              --the salted 32byte hash is stored as a 64char hexstring
    
    primary key (username)
);

CREATE TABLE admin_account (
    username varchar(16) not null,
    
    foreign key (username) references account(username),
    
    primary key (username)
);

CREATE TABLE viewer_account (
    username varchar(16) not null,
    email varchar(254) not null, --maximum length for a valid email address is 254 according to rfc3696
    nickname varchar(16),
    first_name varchar(32),
    last_name varchar(32),
    
    --TODO check username not an admin
    
    foreign key (username) references account(username),
    
    primary key (username)
);

CREATE TABLE genre (
    genre_title varchar(16) not null,
    primary key (genre_title)
);

CREATE TABLE age_rating (
    rating varchar(16) not null,
    primary key (rating)
);

CREATE TABLE film_professional (
    prof_id integer not null,
    first_name varchar(32) not null,
    last_name varchar(32) not null,
    birthdate date,
    primary key (prof_id)
);

CREATE TABLE amenity (
    amenity_type varchar(32) not null,
    
    primary key (amenity_type)
);

CREATE TABLE cinema (
    cinema_id integer not null,
    capacity integer not null,
    
    constraint capacity_nonnegative check (capacity >= 0),
    
    primary key (cinema_id)
);

CREATE TABLE viewers_rate_movies (
    username varchar(16) not null,
    title varchar(255) not null,
    release_date date not null,
    
    foreign key (title, release_date) references movie(title, release_date),
    
    primary key (username, title, release_date)
);

CREATE TABLE movies_have_genres (
    title varchar(255) not null,
    release_date date not null,
    genre_title varchar(16) not null,
    
    foreign key (genre_title) references genre(genre_title),
    foreign key (title, release_date) references movie(title, release_date),
    
    primary key (title, release_date, genre_title)
);

CREATE TABLE actors_act_in_movies (
    prof_id integer not null,
    title varchar(255) not null,
    release_date date not null,
    roletype varchar(5),
    
    constraint roletype_check check (roletype='minor' or roletype='lead'),
    
    foreign key (prof_id) references film_professional(prof_id),
    foreign key (title, release_date) references movie(title, release_date),
    
    primary key (prof_id, title, release_date)
);

--movies generally have one director (but not always) hence the need for the table
CREATE TABLE directors_direct_movies (
    prof_id integer not null,
    title varchar(255) not null,
    release_date date not null,
    
    foreign key (prof_id) references film_professional(prof_id),
    foreign key (title, release_date) references movie(title, release_date),
    
    primary key (prof_id, title, release_date)
);

CREATE TABLE movies_have_age_ratings (
    title varchar(255) not null,
    release_date date not null,
    rating varchar(16) not null,
    
    foreign key (rating) references age_rating(rating),
    foreign key (title, release_date) references movie(title, release_date),
    
    primary key (title, release_date, rating)
);

CREATE TABLE cinemas_have_amenities (
    cinema_id integer not null,
    amenity varchar(32) not null,
    
    foreign key (cinema_id) references cinema(cinema_id),
    foreign key (amenity) references amenity(amenity_type),
    
    primary key (cinema_id, amenity)
);

CREATE TABLE movies_screen_in_cinemas (
    title varchar(255) not null,
    release_date date not null,
    cinema_id integer not null,
    screening_time timestamp not null,
    
    foreign key (title, release_date) references movie(title, release_date),
    foreign key (cinema_id) references cinema(cinema_id),
    
    primary key (title, release_date, cinema_id, screening_time)
);

CREATE TABLE viewer_accounts_book_screenings (
    username varchar(16) not null, --a user can only make one booking for a screening
    title varchar(255) not null,
    release_date date not null,
    cinema_id integer not null,
    screening_time timestamp not null,
    num_seats integer not null,
    
    constraint num_seats_positive check (num_seats > 0),
    
    foreign key (title, release_date) references movie(title, release_date),
    foreign key (title, release_date, cinema_id, screening_time) references
            movies_screen_in_cinemas(title, release_date, cinema_id, screening_time),
    
    primary key (username, title, release_date, cinema_id, screening_time)
);
