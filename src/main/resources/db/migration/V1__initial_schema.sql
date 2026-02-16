CREATE TABLE books(
    id SERIAL PRIMARY KEY NOT NULL,
    book_id UUID DEFAULT gen_random_uuid() UNIQUE,
    title varchar(50) NOT NULL,
    isbn varchar(13) NOT NULL
);

CREATE TABLE copies(
    id SERIAL PRIMARY KEY NOT NULL,
    copy_id UUID DEFAULT gen_random_uuid() UNIQUE,
    book_id INT NOT NULL,
    CONSTRAINT fk_book FOREIGN KEY(book_id) REFERENCES books(id)
);

CREATE TABLE loans(
    id SERIAL PRIMARY KEY NOT NULL,
    copy_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    expected_returned_date TIMESTAMP NOT NULL DEFAULT NOW() + INTERVAL '1 week',
    returned_at TIMESTAMP,
    CONSTRAINT fk_copy FOREIGN KEY(copy_id) REFERENCES copies(id)
);