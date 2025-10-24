CREATE TABLE profiles (
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    avatar VARCHAR(255),
    phone VARCHAR(50),
    bio TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NULL
);

CREATE INDEX idx_profiles_user_id ON profiles(user_id);