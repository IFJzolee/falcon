CREATE TABLE palindrome
(
    id               SERIAL PRIMARY KEY,
    content          VARCHAR(1000) NOT NULL,
    timestamp        TIMESTAMP WITH TIME ZONE NOT NULL,
    timestamp_offset VARCHAR(6) NOT NULL
);
