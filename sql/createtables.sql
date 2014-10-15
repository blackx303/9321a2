DROP TABLE viewer_accounts_book_screenings;
DROP TABLE movies_screen_in_cinemas;
DROP TABLE cinemas_have_amenities;
DROP TABLE movies_have_genres;
DROP TABLE viewers_rate_movies;
DROP TABLE cinemas;
DROP TABLE amenities;
DROP TABLE genres;
DROP TABLE viewer_accounts;
DROP TABLE admin_accounts;
DROP TABLE pending_accounts;
DROP TABLE accounts;
DROP TABLE movies;
DROP TABLE age_ratings;

CREATE TABLE age_ratings (
    rating varchar(16) not null,
    primary key (rating)
);

CREATE TABLE movies (
    title varchar(255) not null,
    release_date date not null,
    age_rating varchar(16) not null,
    director varchar(64),
    actors varchar(255),
    synopsis varchar(1024),
    poster blob not null,
    poster_mimetype varchar(32),
    
    foreign key (age_rating) references age_ratings(rating),
    
    primary key (title, release_date)
);

CREATE TABLE accounts (
    username varchar(16) not null,
    salt char(64) not null, --salt is 32 bytes long, represented as a 64char hexstring
    password_and_salt_hash char(64) not null, --we use sha256(concat(salt, password))
                                              --salt is changed on password change
                                              --the salted 32byte hash is stored as a 64char hexstring
    
    primary key (username)
);

CREATE TABLE pending_accounts (
    username varchar(16) not null,
    
    email varchar(254) not null,
    confirmation_key char(64) not null,
    created_at timestamp,
    
    foreign key (username) references accounts(username),
    primary key (username)
);

CREATE TABLE admin_accounts (
    username varchar(16) not null,
    
    foreign key (username) references accounts(username),
    
    primary key (username)
);

CREATE TABLE viewer_accounts (
    username varchar(16) not null,
    email varchar(254) not null, --maximum length for a valid email address is 254 according to rfc3696
    nickname varchar(16),
    first_name varchar(32),
    last_name varchar(32),
    
    --TODO check username not an admin
    
    foreign key (username) references accounts(username),
    
    primary key (username)
);

CREATE TABLE genres (
    genre_title varchar(16) not null,
    primary key (genre_title)
);

CREATE TABLE amenities (
    amenity_type varchar(32) not null,
    
    primary key (amenity_type)
);

CREATE TABLE cinemas (
    location varchar(64) not null,
    capacity integer not null,
    
    constraint capacity_nonnegative check (capacity >= 0),
    
    primary key (location)
);

CREATE TABLE viewers_rate_movies (
    username varchar(16) not null,
    title varchar(255) not null,
    release_date date not null,
    
    rating integer not null,
    review_text varchar(1024) not null,
    
    constraint rating_is_star_rating check (rating > 0 and rating <= 5),
    
    foreign key (title, release_date) references movies(title, release_date),
    
    primary key (username, title, release_date)
);

CREATE TABLE movies_have_genres (
    title varchar(255) not null,
    release_date date not null,
    genre_title varchar(16) not null,
    
    foreign key (genre_title) references genres(genre_title),
    foreign key (title, release_date) references movies(title, release_date),
    
    primary key (title, release_date, genre_title)
);

CREATE TABLE cinemas_have_amenities (
    location varchar(64) not null,
    amenity varchar(32) not null,
    
    foreign key (location) references cinemas(location),
    foreign key (amenity) references amenities(amenity_type),
    
    primary key (location, amenity)
);

CREATE TABLE movies_screen_in_cinemas (
    title varchar(255) not null,
    release_date date not null,
    location varchar(64) not null,
    screening_time timestamp not null,
    
    foreign key (title, release_date) references movies(title, release_date),
    foreign key (location) references cinemas(location),
    
    primary key (title, release_date, location, screening_time)
);

CREATE TABLE viewer_accounts_book_screenings (
    username varchar(16) not null, --a user can only make one booking for a screening
    title varchar(255) not null,
    release_date date not null,
    location varchar(64) not null,
    screening_time timestamp not null,
    num_seats integer not null,
    
    constraint num_seats_positive check (num_seats > 0),
    
    foreign key (title, release_date) references movies(title, release_date),
    foreign key (title, release_date, location, screening_time) references
            movies_screen_in_cinemas(title, release_date, location, screening_time),
    
    primary key (username, title, release_date, location, screening_time)
);
