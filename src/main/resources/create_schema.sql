
CREATE TABLE groups(
    id serial PRIMARY KEY,
    name text NOT NULL,
    type text NOT NULL,
    parent_id integer NOT NULL
);

CREATE TABLE users(
    id serial PRIMARY KEY,
    name text NOT NULL,
    group_id integer NOT NULL REFERENCES groups (id),
    email text NOT NULL,
    phone text,
    UNIQUE (email)
);

CREATE TABLE events(
    id text NOT NULL,
    payload text NOT NULL,
    user_id integer NOT NULL  REFERENCES users (id),
    type text NOT NULL,
    created timestamp with time zone,
    source text,
    CONSTRAINT pk_events PRIMARY KEY (id)
);
