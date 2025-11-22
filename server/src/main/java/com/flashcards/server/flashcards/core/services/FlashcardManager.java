package com.flashcards.server.flashcards.core.services;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import com.flashcards.server.flashcards.core.dto.*;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.entities.Tag;
import com.flashcards.server.flashcards.core.ports.repository.FlashcardRepositoryPort;
import com.flashcards.server.flashcards.core.ports.repository.TagRepositoryPort;
import com.flashcards.server.flashcards.core.ports.services.FlashcardManagerPort;
import com.flashcards.server.flashcards.core.services.mappers.FlashcardMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FlashcardManager implements FlashcardManagerPort {
    private final FlashcardRepositoryPort flashcardRepositoryPort;
    private final TagRepositoryPort tagRepositoryPort;

    public FlashcardManager(FlashcardRepositoryPort flashcardRepositoryPort, TagRepositoryPort tagRepositoryPort) {
        this.flashcardRepositoryPort = flashcardRepositoryPort;
        this.tagRepositoryPort = tagRepositoryPort;
    }

    @Override
    public FlashcardDetailsDTO create(CreateFlashcardDTO createFlashcardDTO, UUID userId) {
        List<Tag> tags = tagRepositoryPort.findByUserIdAndIds(userId, createFlashcardDTO.tagIds());

        if (tags.size() != createFlashcardDTO.tagIds().size()) {
            throwInvalidTagIdException();
        }

        Flashcard flashcard = new Flashcard(
                userId,
                createFlashcardDTO.textQuestion(),
                createFlashcardDTO.urlQuestion(),
                createFlashcardDTO.answer()
        );

        for (Tag tag : tags) {
            flashcard.addTag(tag);
        }

        flashcardRepositoryPort.create(flashcard);

        return FlashcardMapper.toFlashcardDetailsDTO(flashcard);
    }

    @Override
    public void deleteById(UUID userId, UUID flashcardId) {
        if (flashcardRepositoryPort.deleteByUserIdAndId(userId, flashcardId) == 0) {
            throw new ApiException(new ApiError(
                    HttpStatus.NOT_FOUND,
                    "NOT_FOUND",
                    Flashcard.class.getSimpleName() + " with id " + flashcardId +
                            " not found for user with " + userId + " id"
            ));
        }
    }

    @Override
    public FlashcardDetailsDTO update(UUID userId, UUID flashcardId, UpdateFlashcardDTO updateFlashcardDTO) {
        Flashcard flashcard = flashcardRepositoryPort.findByUserIdAndId(userId, flashcardId)
                .orElseThrow(() -> new ApiException(new ApiError(
                        HttpStatus.BAD_REQUEST,
                        "INVALID_FLASHCARD_ID",
                        "There is no entities with provided ID"
                )));

        applyPatch(userId, flashcard, updateFlashcardDTO);

        return FlashcardMapper.toFlashcardDetailsDTO(flashcardRepositoryPort.update(flashcard.getId(), flashcard));
    }

    private void applyPatch(UUID userId, Flashcard flashcard, UpdateFlashcardDTO updateFlashcardDTO) {
        if (updateFlashcardDTO.textQuestion() != null) {
            flashcard.setTextQuestion(updateFlashcardDTO.textQuestion());
        }

        if (updateFlashcardDTO.urlQuestion() != null) {
            flashcard.setUrlQuestion(updateFlashcardDTO.urlQuestion());
        }

        if (updateFlashcardDTO.answer() != null) {
            flashcard.setAnswer(updateFlashcardDTO.answer());
        }

        if (updateFlashcardDTO.removeTagIds() != null) {
            List<Tag> tags = tagRepositoryPort.findByUserIdAndIds(userId, updateFlashcardDTO.removeTagIds());

            if (tags.size() != updateFlashcardDTO.removeTagIds().size()) {
                throwInvalidTagIdException();
            }

            for (Tag tag : tags) {
                flashcard.removeTag(tag);
            }
        }

        if (updateFlashcardDTO.addTagIds() != null) {
            List<Tag> tags = tagRepositoryPort.findByUserIdAndIds(userId, updateFlashcardDTO.addTagIds());

            if (tags.size() != updateFlashcardDTO.addTagIds().size()) {
                throwInvalidTagIdException();
            }

            for (Tag tag : tags) {
                flashcard.addTag(tag);
            }
        }
    }

    private static void throwInvalidTagIdException() {
        throw new ApiException(new ApiError(
                HttpStatus.BAD_REQUEST,
                "INVALID_TAG_ID",
                "One or more provided tag IDs are invalid or non-existent"
        ));
    }
}
