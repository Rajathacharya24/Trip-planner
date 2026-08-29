UPDATE users
SET role = 'USER'
WHERE role IS NULL
   OR role NOT IN ('USER', 'ADMIN');

ALTER TABLE users
    ALTER COLUMN role SET DEFAULT 'USER';

ALTER TABLE users
    ADD CONSTRAINT users_role_check
    CHECK (role IN ('USER', 'ADMIN'));