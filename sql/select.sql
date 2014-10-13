SELECT *
FROM movie m
INNER JOIN movies_have_genres mg ON mg.title=m.title
WHERE m.title = "shrek"

SELECT *
FROM movie m
INNER JOIN movies_have_genres mg ON mg.title=m.title
WHERE m.title LIKE '%shrek%'
OR mg.genre_title LIKE '%Action%';