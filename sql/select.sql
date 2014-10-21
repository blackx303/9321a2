SELECT genre_title 
FROM movies m 
INNER JOIN movies_have_genres mg ON mg.title=m.title 
WHERE LOWER(m.title) = LOWER('gone girl')

SELECT * from movies LIMIT 5;

SELECT * FROM movies WHERE release_date < CURRENT DATE ORDER BY release_date DESC
SELECT * FROM movies WHERE release_date > CURRENT DATE ORDER BY release_date