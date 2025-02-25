CREATE TABLE usersAxon.association_value_entry (
    id BIGSERIAL PRIMARY KEY,
    association_key VARCHAR(255) NOT NULL,
    association_value VARCHAR(255) NOT NULL,
    saga_id VARCHAR(255) NOT NULL,
    saga_type VARCHAR(255) NOT NULL
);

CREATE INDEX idx_association_value_entry_key_value ON association_value_entry (association_key, association_value);
CREATE INDEX idx_association_value_entry_saga_id ON association_value_entry (saga_id);