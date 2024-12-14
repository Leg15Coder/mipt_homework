-- Создание таблицы статей
CREATE TABLE articles (
    article_id BIGINT PRIMARY KEY,
    header VARCHAR(1024) NOT NULL,
    trending BOOLEAN NOT NULL,
    tags TEXT[] NOT NULL
);

-- Создание таблицы комментариев
CREATE TABLE comment (
    comment_id BIGINT PRIMARY KEY,
    article_id BIGINT REFERENCES articles(article_id) NOT NULL,
    content TEXT NOT NULL
);
