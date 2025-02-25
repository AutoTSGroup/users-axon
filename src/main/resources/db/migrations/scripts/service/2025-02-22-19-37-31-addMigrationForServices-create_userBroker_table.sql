CREATE TABLE users_axon.user_broker (
    broker_id UUID PRIMARY KEY,
    user_id INTEGER REFERENCES trader_bot_users(id) ON DELETE CASCADE
);