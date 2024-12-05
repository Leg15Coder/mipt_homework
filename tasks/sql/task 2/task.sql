SELECT post_id FROM post
    WHERE post_id IN
        (SELECT post_id FROM comment WHERE post_id=post_id GROUP BY post_id HAVING count(*) = 2)
        AND length(content) > 20
        AND LEFT(title, 1) ~ '^[0-9]'
ORDER BY post_id;