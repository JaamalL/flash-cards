package com.flashcards.server.flashcards.core.services;

import com.flashcards.server.common.error.ApiError;
import com.flashcards.server.common.exceptions.ApiException;
import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;
import com.flashcards.server.flashcards.core.dto.UpdateTagDTO;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.entities.Tag;
import com.flashcards.server.flashcards.core.ports.repository.TagRepositoryPort;
import com.flashcards.server.flashcards.core.ports.services.TagManagerPort;
import com.flashcards.server.flashcards.core.services.mappers.TagMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class TagManager implements TagManagerPort {
    private final TagRepositoryPort tagRepositoryPort;

    public TagManager(TagRepositoryPort tagRepositoryPort) {
        this.tagRepositoryPort = tagRepositoryPort;
    }

    @Override
    public TagDetailsDTO create(CreateTagDTO createTagDTO, UUID userId) {
        Tag tag = new Tag(
                userId,
                createTagDTO.name(),
                createTagDTO.description()
        );

        tagRepositoryPort.create(tag);

        return TagMapper.toTagDetailsDTO(tag);
    }

    @Override
    public void deleteById(UUID userId, UUID tagId) {
        if (tagRepositoryPort.deleteByUserIdAndId(userId, tagId) == 0) {
            throw new ApiException(new ApiError(
                    HttpStatus.NOT_FOUND,
                    "NOT_FOUND",
                    Tag.class.getSimpleName() + " with id " + tagId +
                            " not found for user with " + userId + " id"
            ));
        }
    }

    @Override
    public TagDetailsDTO update(UUID userId, UUID tagId, UpdateTagDTO updateTagDTO) {
        Tag tag = tagRepositoryPort.findByUserIdAndId(userId, tagId)
                .orElseThrow(() -> new ApiException(new ApiError(
                        HttpStatus.BAD_REQUEST,
                        "INVALID_TAG_ID",
                        "There is no entities with provided ID"
                )));

        applyPatch(tag, updateTagDTO);

        return TagMapper.toTagDetailsDTO(tagRepositoryPort.update(tag.getId(), tag));
    }

    private void applyPatch(Tag entity, UpdateTagDTO updateTagDTO) {
        if (updateTagDTO.name() != null) {
            entity.setName(updateTagDTO.name());
        }

        if (updateTagDTO.description() != null) {
            entity.setDescription(updateTagDTO.description());
        }
    }
}
