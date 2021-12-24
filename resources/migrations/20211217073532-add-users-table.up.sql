CREATE TABLE users
(login TEXT PRIMARY KEY,
 password VARCHAR(300),
 created_at TIMESTAMP NOT NULL DEFAULT now()
 );
