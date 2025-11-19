package com.flashcards.server.flashcards.core.services;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDetailsDTO;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.entities.Tag;
import com.flashcards.server.flashcards.core.enums.TagMatchMode;
import com.flashcards.server.flashcards.core.ports.repository.FlashcardRepositoryPort;
import com.flashcards.server.flashcards.core.ports.repository.TagRepositoryPort;
import com.flashcards.server.flashcards.core.ports.services.FlashcardQueryPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FlashcardQuery implements FlashcardQueryPort {
    private final FlashcardRepositoryPort flashcardRepositoryPort;
    private final TagRepositoryPort tagRepositoryPort;

    public FlashcardQuery(
            FlashcardRepositoryPort flashcardRepositoryPort,
            TagRepositoryPort tagRepositoryPort
    ) {
        this.flashcardRepositoryPort = flashcardRepositoryPort;
        this.tagRepositoryPort = tagRepositoryPort;
    }

    @Override
    public List<FlashcardDTO> getByUserId(UUID userId) {
        List<Flashcard> flashcards = flashcardRepositoryPort.findByUserId(userId);

        return flashcards.stream().map(flashcard ->
                new FlashcardDTO(
                        flashcard.getId(),
                        flashcard.getTextQuestion(),
                        flashcard.getUrlQuestion(),
                        flashcard.getAnswer(),
                        flashcard.getTags().stream().map(Tag::getId).toList()
                )
        ).toList();
    }

    @Override
    public FlashcardDetailsDTO getById(UUID userId, UUID flashcardId) {
        Flashcard flashcard = flashcardRepositoryPort.findByUserIdAndId(userId, flashcardId)
                .orElseThrow(() -> new ApiException(new ApiError(
                        HttpStatus.BAD_REQUEST,
                        "INVALID_FLASHCARD_ID",
                        "There is no entities with provided ID"
                )));

        return new FlashcardDetailsDTO(
                flashcard.getId(),
                flashcard.getTextQuestion(),
                flashcard.getUrlQuestion(),
                flashcard.getAnswer(),
                flashcard.getTags().stream().map(Tag::getId).toList(),
                flashcard.getCreatedAt().toString()
        );
    }

    @Override
    public List<FlashcardDTO> getByTagIds(UUID userId, List<UUID> tagIds, TagMatchMode tagMatchMode) {
        if (tagRepositoryPort.countByUserIdAndIds(userId, tagIds) != tagIds.size()) {
            throw new ApiException(new ApiError(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_TAG_ID",
                    "One or more provided tag IDs are invalid or non-existent"
            ));
        }

        List<Flashcard> flashcards = switch (tagMatchMode) {
            case ALL -> flashcardRepositoryPort.findByUserIdAndAllTagIds(userId, tagIds);
            case ANY -> flashcardRepositoryPort.findByUserIdAndAnyTagIds(userId, tagIds);
        };

        return flashcards.stream().map(flashcard ->
                new FlashcardDTO(
                        flashcard.getId(),
                        flashcard.getTextQuestion(),
                        flashcard.getUrlQuestion(),
                        flashcard.getAnswer(),
                        flashcard.getTags().stream().map(Tag::getId).toList()
                )
        ).toList();
    }
}
