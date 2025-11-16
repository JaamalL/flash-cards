package com.flashcards.server.flashcards.core.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.flashcards.server.common.entities.Base;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "flashcards",
        indexes =
        {
                @Index(name = "idx_flashcards_user_id", columnList = "user_id")
        }
)
public class Flashcard extends Base {
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "text_question")
    private String textQuestion;

    @Column(name = "url_question")
    private String urlQuestion;

    @Column(name = "answer")
    private String answer;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "flashcard_tags",
            joinColumns = @JoinColumn(name = "flashcard_id"),
            inverseJoinColumns = @JoinColumn(name = "flashcard_tag_id")
    )
    @JsonManagedReference
    private Set<Tag> tags = new HashSet<>();

    protected Flashcard() {
    }

    public Flashcard(
            final UUID userId,
            String textQuestion,
            String urlQuestion,
            String answer
    ) {
        super();
        this.userId = userId;
        this.textQuestion = textQuestion;
        this.urlQuestion = urlQuestion;
        this.answer = answer;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTextQuestion() {
        return textQuestion;
    }

    public String getUrlQuestion() {
        return urlQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
    }

    public void setUrlQuestion(String urlQuestion) {
        this.urlQuestion = urlQuestion;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getFlashcards().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getFlashcards().remove(this);
    }
}
