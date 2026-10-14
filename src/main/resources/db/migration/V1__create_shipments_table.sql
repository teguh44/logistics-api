CREATE TABLE shipments (
    id BIGSERIAL PRIMARY KEY,
    tracking_number VARCHAR(255),
    origin VARCHAR(255),
    destination VARCHAR(255),
    status VARCHAR(255) NOT NULL
);