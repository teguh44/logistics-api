CREATE TABLE shipment_tracking (
    id BIGSERIAL PRIMARY KEY,
    shipment_id BIGINT NOT NULL,
    status VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_shipment_tracking_shipment
        FOREIGN KEY (shipment_id)
        REFERENCES shipments(id)
);