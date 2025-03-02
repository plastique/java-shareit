CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT true,
    owner_id BIGINT NOT NULL,
    request_id BIGINT NOT NULL,
    CONSTRAINT pk_items PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id BIGINT NOT NULL,
    booker_id BIGINT NOT NULL,
    status VARCHAR(255),
    CONSTRAINT pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description TEXT NOT NULL,
    requestor_id BIGINT NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text TEXT NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);