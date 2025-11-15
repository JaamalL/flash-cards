CREATE TABLE credentials_accounts (
    id UUID PRIMARY KEY NOT NULL,
    hashed_password TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    
    CONSTRAINT fk_account FOREIGN KEY(id) REFERENCES accounts(id) ON DELETE CASCADE
);