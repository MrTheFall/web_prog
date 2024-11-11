CREATE TABLE check_results (
    id SERIAL PRIMARY KEY,
    session_id VARCHAR(128) NOT NULL,
    x INT NOT NULL,
    y DOUBLE PRECISION NOT NULL,
    r INT NOT NULL,
    hit BOOLEAN NOT NULL,
    check_time VARCHAR(128) NOT NULL,
    execution_time BIGINT NOT NULL
);
