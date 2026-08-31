ALTER TABLE packages
    ADD COLUMN IF NOT EXISTS destination VARCHAR(255);

UPDATE packages
SET destination = CASE
    WHEN LOWER(name) LIKE '%luxury%' THEN 'Dubai'
    WHEN LOWER(name) LIKE '%ordinary%' OR LOWER(name) LIKE '%simple%' THEN 'Kerala'
    ELSE COALESCE(destination, name)
END
WHERE destination IS NULL;

ALTER TABLE packages
    ALTER COLUMN destination SET NOT NULL;

ALTER TABLE bookings
    ADD COLUMN IF NOT EXISTS user_id BIGINT,
    ADD COLUMN IF NOT EXISTS package_id BIGINT,
    ADD COLUMN IF NOT EXISTS destination VARCHAR(255),
    ADD COLUMN IF NOT EXISTS departure_date DATE,
    ADD COLUMN IF NOT EXISTS return_date DATE,
    ADD COLUMN IF NOT EXISTS adults INTEGER,
    ADD COLUMN IF NOT EXISTS children INTEGER,
    ADD COLUMN IF NOT EXISTS rooms INTEGER,
    ADD COLUMN IF NOT EXISTS total_amount DECIMAL(19,2),
    ADD COLUMN IF NOT EXISTS status VARCHAR(50),
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

UPDATE bookings
SET package_id = (
        SELECT p.id
        FROM packages p
        WHERE LOWER(p.name) = LOWER(bookings.package_name)
    )
WHERE package_id IS NULL;

UPDATE bookings
SET user_id = (
        SELECT u.id
        FROM users u
        WHERE LOWER(u.email) = LOWER(bookings.email)
    )
WHERE user_id IS NULL;

UPDATE bookings
SET destination = (
        SELECT p.destination
        FROM packages p
        WHERE p.id = bookings.package_id
    )
WHERE destination IS NULL;

UPDATE bookings
SET departure_date = COALESCE(departure_date, CURRENT_DATE),
    return_date = COALESCE(return_date, CURRENT_DATE + 1),
    adults = COALESCE(adults, 1),
    children = COALESCE(children, 0),
    rooms = COALESCE(rooms, 1),
    total_amount = COALESCE(total_amount, (
        SELECT p.price
        FROM packages p
        WHERE p.id = bookings.package_id
    )),
    status = COALESCE(status, 'PENDING'),
    created_at = COALESCE(created_at, CURRENT_TIMESTAMP),
    updated_at = COALESCE(updated_at, CURRENT_TIMESTAMP)
WHERE departure_date IS NULL
   OR return_date IS NULL
   OR adults IS NULL
   OR children IS NULL
   OR rooms IS NULL
   OR total_amount IS NULL
   OR status IS NULL
   OR created_at IS NULL
   OR updated_at IS NULL;

ALTER TABLE bookings
    ALTER COLUMN user_id SET NOT NULL,
    ALTER COLUMN package_id SET NOT NULL,
    ALTER COLUMN destination SET NOT NULL,
    ALTER COLUMN departure_date SET NOT NULL,
    ALTER COLUMN return_date SET NOT NULL,
    ALTER COLUMN adults SET NOT NULL,
    ALTER COLUMN children SET NOT NULL,
    ALTER COLUMN rooms SET NOT NULL,
    ALTER COLUMN total_amount SET NOT NULL,
    ALTER COLUMN status SET NOT NULL,
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE bookings
    ADD CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users (id),
    ADD CONSTRAINT fk_bookings_package FOREIGN KEY (package_id) REFERENCES packages (id);
