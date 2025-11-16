package com.flashcards.server.flashcards.core.services;

import com.flashcards.server.flashcards.core.dto.CreateTagDTO;
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
    public Map<String, String> createTag(CreateTagDTO createTagDTO, UUID userId) {
        Tag entity = new Tag(
                userId,
                createTagDTO.name(),
                createTagDTO.description()
        );

        tagRepositoryPort.create(entity);

        Map<String, String> result = new HashMap<>();
        result.put("id", entity.getId().toString());
        result.put("name", entity.getName());
        result.put("description", entity.getDescription());

        return result;
    }
}
