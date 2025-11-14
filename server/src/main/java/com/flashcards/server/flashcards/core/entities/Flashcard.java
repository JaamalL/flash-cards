package com.flashcards.server.flashcards.core.entities;

import com.flashcards.server.common.entities.Base;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

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

    public void setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
    }

    public void setUrlQuestion(String urlQuestion) {
        this.urlQuestion = urlQuestion;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
