CREATE TABLE flashcards (
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID NOT NULL,
    text_question TEXT NOT NULL,
    url_question VARCHAR(2048),
    answer TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE INDEX idx_flashcards_user_id ON flashcards(user_id);
