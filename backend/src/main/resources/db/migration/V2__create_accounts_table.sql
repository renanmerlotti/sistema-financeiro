CREATE TABLE accounts(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0,
    account_type VARCHAR(100) NOT NULL,
    user_id BIGINT,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);