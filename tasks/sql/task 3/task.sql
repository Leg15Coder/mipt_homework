SELECT * FROM post
    WHERE post_id IN
        (SELECT post_id FROM comment
		WHERE post_id=post_id GROUP BY post_id
		HAVING count(*) < 2)
ORDER BY post_id limit 10;