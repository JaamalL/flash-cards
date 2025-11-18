package com.flashcards.server.flashcards.core.services;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import com.flashcards.server.flashcards.core.dto.FlashcardDTO;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.entities.Tag;
import com.flashcards.server.flashcards.core.ports.repository.FlashcardRepositoryPort;
import com.flashcards.server.flashcards.core.ports.repository.TagRepositoryPort;
import com.flashcards.server.flashcards.core.ports.services.CreateFlashcardPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CreateFlashcard implements CreateFlashcardPort {
    private final FlashcardRepositoryPort flashcardRepository;
    private final TagRepositoryPort tagRepositoryPort;

    public CreateFlashcard(FlashcardRepositoryPort flashcardRepository, TagRepositoryPort tagRepositoryPort) {
        this.flashcardRepository = flashcardRepository;
        this.tagRepositoryPort = tagRepositoryPort;
    }

    @Override
    public FlashcardDTO createFlashcard(CreateFlashcardDTO createFlashcardDTO, UUID userId) {
        List<Tag> tags = tagRepositoryPort.findAllById(createFlashcardDTO.tagIds());

        if (tags.size() != createFlashcardDTO.tagIds().size()) {
            throw new ApiException(new ApiError(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_TAG_ID",
                    "One or more provided tag IDs are invalid or non-existent"
            ));
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

        flashcardRepository.create(flashcard);

        return new FlashcardDTO(
                flashcard.getId(),
                flashcard.getTextQuestion(),
                flashcard.getUrlQuestion(),
                flashcard.getAnswer(),
                createFlashcardDTO.tagIds()
        );
    }
}
