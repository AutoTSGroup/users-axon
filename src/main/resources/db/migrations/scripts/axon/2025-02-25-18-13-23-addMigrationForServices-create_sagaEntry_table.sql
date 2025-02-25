CREATE TABLE users_axon.saga_entry (
    saga_id VARCHAR(255) PRIMARY KEY,
    revision VARCHAR(255),
    saga_type VARCHAR(255) NOT NULL,
    serialized_saga BIGINT
);

CREATE INDEX idx_saga_entry_saga_type ON saga_entry (saga_type);