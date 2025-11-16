CREATE TABLE tags (
    id UUID PRIMARY KEY NOT NULL,
    user_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE INDEX idx_tags_user_id ON tags(user_id);

CREATE TABLE flashcard_tags (
    flashcard_id UUID NOT NULL,
    tag_id UUID NOT NULL,
    PRIMARY KEY (tag_id, flashcard_id),
    CONSTRAINT fk_flashcard FOREIGN KEY(flashcard_id) REFERENCES flashcards(id) ON DELETE CASCADE,
    CONSTRAINT fk_tag FOREIGN KEY(tag_id) REFERENCES tags(id) ON DELETE CASCADE
);
