package com.flashcards.server.flashcards.core.services;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDetailsDTO;
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
    private final FlashcardRepositoryPort flashcardRepository;
    private final TagRepositoryPort tagRepositoryPort;

    public FlashcardManager(FlashcardRepositoryPort flashcardRepository, TagRepositoryPort tagRepositoryPort) {
        this.flashcardRepository = flashcardRepository;
        this.tagRepositoryPort = tagRepositoryPort;
    }

    @Override
    public FlashcardDetailsDTO create(CreateFlashcardDTO createFlashcardDTO, UUID userId) {
        if (tagRepositoryPort.countByUserIdAndIds(userId, createFlashcardDTO.tagIds()) !=
                createFlashcardDTO.tagIds().size()) {
            throw new ApiException(new ApiError(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_TAG_ID",
                    "One or more provided tag IDs are invalid or non-existent"
            ));
        }

        List<Tag> tags = tagRepositoryPort.findByIds(createFlashcardDTO.tagIds());

        Flashcard flashcard = new Flashcard(
                userId,
                createFlashcardDTO.textQuestion(),
                createFlashcardDTO.urlQuestion(),
                createFlashcardDTO.answer()
        );

        for (Tag tag : tags) {
            flashcard.addTag(tag);
        }

        flashcardRepository.create(flashcard);

        return FlashcardMapper.toFlashcardDetailsDTO(flashcard);
    }

    @Override
    public void deleteById(UUID userId, UUID flashcardId) {
        if (flashcardRepository.deleteByUserIdAndId(userId, flashcardId) == 0) {
            throw new ApiException(new ApiError(
                    HttpStatus.NOT_FOUND,
                    "NOT_FOUND",
                    Flashcard.class.getSimpleName() + " with id " + flashcardId +
                            " not found for user with " + userId + " id"
            ));
        }
    }
}
