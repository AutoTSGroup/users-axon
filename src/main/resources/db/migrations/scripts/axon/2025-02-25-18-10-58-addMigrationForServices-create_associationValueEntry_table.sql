-- Переключаемся на схему users_axon
SET search_path TO users_axon;

-- Создаем последовательность для генерации ID
CREATE SEQUENCE association_value_entry_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Создаем таблицу association_value_entry
CREATE TABLE association_value_entry (
    id BIGINT NOT NULL DEFAULT nextval('association_value_entry_seq'),  -- Используем последовательность
    association_key VARCHAR(255) NOT NULL,
    association_value VARCHAR(255) NOT NULL,
    saga_id VARCHAR(255) NOT NULL,
    saga_type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- Создаем индексы для ускорения поиска
CREATE INDEX idx_association_value_entry_key_value ON association_value_entry (association_key, association_value);
CREATE INDEX idx_association_value_entry_saga_id ON association_value_entry (saga_id);