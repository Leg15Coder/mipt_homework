CREATE OR REPLACE PROCEDURE insertComment(comment_id BIGINT, post_id BIGINT, profile_id BIGINT)
    LANGUAGE plpgsql
AS
$$
BEGIN

    INSERT INTO comment(comment_id, post_id, profile_id, date_commented)
    VALUES (comment_id, post_id, profile_id, CURRENT_TIMESTAMP);
END;
$$;

do
$$
    BEGIN
        FOR profile_id in 1..55
            LOOP
                INSERT INTO profile(profile_id, login, date_registered)
                VALUES (profile_id, 'login' || profile_id, CURRENT_TIMESTAMP);
            END LOOP;
        FOR post_id in 1..50
            LOOP
                INSERT INTO post(post_id, title, content, profile_id, date_added)
                VALUES (post_id,
                        CASE WHEN post_id % 2 = 0 THEN post_id || 'post' ELSE 'post' || post_id END,
                        repeat('a', post_id),
                        post_id,
                        CURRENT_TIMESTAMP);
            END LOOP;
        FOR comment_id in 1..50
            LOOP
                IF comment_id <= 45
                THEN
                    CALL insertComment(comment_id, comment_id, comment_id);
                ELSE
                    CONTINUE;
                END IF;

                IF comment_id % 2 = 0
                THEN
                    CALL insertComment(comment_id * 100, comment_id, comment_id);
                END IF;

                if comment_id % 10 = 0
                THEN
                    CALL insertComment(comment_id * 1000, comment_id, comment_id);
                END IF;
            END LOOP;
    END
$$;