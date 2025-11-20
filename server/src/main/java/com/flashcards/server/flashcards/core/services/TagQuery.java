package com.flashcards.server.flashcards.core.services;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.entities.Tag;
import com.flashcards.server.flashcards.core.ports.repository.TagRepositoryPort;
import com.flashcards.server.flashcards.core.ports.services.TagQueryPort;
import com.flashcards.server.flashcards.core.services.mappers.TagMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TagQuery implements TagQueryPort {
    private final TagRepositoryPort tagRepositoryPort;

    public TagQuery(TagRepositoryPort tagRepositoryPort) {
        this.tagRepositoryPort = tagRepositoryPort;
    }

    @Override
    public List<TagDTO> getByUserId(UUID userId) {
        List<Tag> tags = tagRepositoryPort.findByUserId(userId);

        return tags.stream().map(TagMapper::toTagDTO).toList();
    }

    @Override
    public TagDetailsDTO getById(UUID userId, UUID tagId) {
        Tag tag = tagRepositoryPort.findByUserIdAndId(userId, tagId)
                .orElseThrow(() -> new ApiException(new ApiError(
                                HttpStatus.BAD_REQUEST,
                                "INVALID_TAG_ID",
                                "There is no entities with provided ID"
                )));

        return TagMapper.toTagDetailsDTO(tag);
    }
}
