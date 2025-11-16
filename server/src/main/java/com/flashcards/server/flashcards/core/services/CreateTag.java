package com.flashcards.server.flashcards.core.services;

import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.entities.Tag;
import com.flashcards.server.flashcards.core.ports.repository.TagRepositoryPort;
import com.flashcards.server.flashcards.core.ports.services.CreateTagPort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CreateTag implements CreateTagPort {
    private final TagRepositoryPort tagRepositoryPort;

    public CreateTag(TagRepositoryPort tagRepositoryPort) {
        this.tagRepositoryPort = tagRepositoryPort;
    }

    @Override
    public TagDTO createTag(CreateTagDTO createTagDTO, UUID userId) {
        Tag entity = new Tag(
                userId,
                createTagDTO.name(),
                createTagDTO.description()
        );

        tagRepositoryPort.create(entity);

        return new TagDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
