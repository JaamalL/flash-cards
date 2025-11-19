package com.flashcards.server.flashcards.core.services;

import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
import com.flashcards.server.flashcards.core.dto.TagDTO;
import com.flashcards.server.flashcards.core.dto.TagDetailsDTO;
import com.flashcards.server.flashcards.core.entities.Tag;
import com.flashcards.server.flashcards.core.ports.repository.TagRepositoryPort;
import com.flashcards.server.flashcards.core.ports.services.TagManagerPort;
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
        Tag entity = new Tag(
                userId,
                createTagDTO.name(),
                createTagDTO.description()
        );

        tagRepositoryPort.create(entity);

        return new TagDetailsDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                new ArrayList<>(),
                entity.getCreatedAt().toString()
        );
    }
}
