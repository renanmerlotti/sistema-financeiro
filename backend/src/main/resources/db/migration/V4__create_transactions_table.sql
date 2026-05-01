CREATE TABLE transactions(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    amount DECIMAL(19,2) NOT NULL,
    date DATE NOT NULL,
    transaction_type VARCHAR(100) NOT NULL,
    account_id BIGINT NOT NULL,
    category_id BIGINT,
    CONSTRAINT fk_transaction_account
        FOREIGN KEY (account_id) REFERENCES accounts(id),
    CONSTRAINT fk_transaction_category
        FOREIGN KEY (category_id) REFERENCES categories(id)
);