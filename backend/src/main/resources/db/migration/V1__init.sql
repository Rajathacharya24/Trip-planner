CREATE TABLE packages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price INT NOT NULL
);

CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    package_name VARCHAR(255) NOT NULL
);

-- Insert some default packages
INSERT INTO packages (name, description, price) VALUES 
('Ordinary Package', 'Hotel Stay, Basic Transport, 2 Meals Daily', 15000),
('Luxury Package', '5-Star Hotel, Private Cab, All Meals Included', 35000);
