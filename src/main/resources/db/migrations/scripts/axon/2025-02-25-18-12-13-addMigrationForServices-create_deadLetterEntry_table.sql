CREATE TABLE usersAxon.dead_letter_entry (
    dead_letter_id VARCHAR(255) PRIMARY KEY,
    message_id VARCHAR(255) NOT NULL,
    cause_message_id VARCHAR(255),
    cause_type VARCHAR(255),
    diagnostics TEXT,
    enqueued_at TIMESTAMP NOT NULL,
    last_touched TIMESTAMP,
    aggregate_identifier VARCHAR(255),
    event_identifier VARCHAR(255) NOT NULL,
    message_type VARCHAR(255) NOT NULL,
    payload_type VARCHAR(255) NOT NULL,
    payload_revision VARCHAR(255),
    payload_data BYTEA NOT NULL,
    meta_data BYTEA NOT NULL,
    sequence_number BIGINT,
    sequence_identifier VARCHAR(255),
    index BIGINT NOT NULL,
    processing_group VARCHAR(255) NOT NULL,
    processing_started TIMESTAMP,
    sequence_identifier_hash VARCHAR(255) NOT NULL
);

CREATE INDEX idx_dead_letter_entry_message_id ON dead_letter_entry (message_id);
CREATE INDEX idx_dead_letter_entry_sequence_identifier ON dead_letter_entry (sequence_identifier);
CREATE INDEX idx_dead_letter_entry_processing_group ON dead_letter_entry (processing_group);