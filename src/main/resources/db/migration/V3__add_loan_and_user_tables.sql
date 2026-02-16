CREATE TABLE users(
    id SERIAL PRIMARY KEY NOT NULL,
    user_id UUID DEFAULT gen_random_uuid() UNIQUE,
    username varchar(50),
    email varchar(50)
);

CREATE TABLE IF NOT EXISTS loans(
    id SERIAL PRIMARY KEY NOT NULL,
    loan_id UUID DEFAULT gen_random_uuid() UNIQUE,
    user_id INT NOT NULL,
    copy_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    expected_returned_date DATE NOT NULL DEFAULT NOW() + INTERVAL '1 week',
    returned_at TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT fk_copy FOREIGN KEY(copy_id) REFERENCES copies(id)
)
