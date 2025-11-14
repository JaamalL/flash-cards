package com.flashcards.server.flashcards.core.services;

import com.flashcards.server.flashcards.core.dto.CreateFlashcardDTO;
import com.flashcards.server.flashcards.core.entities.Flashcard;
import com.flashcards.server.flashcards.core.ports.repository.FlashcardRepositoryPort;
import com.flashcards.server.flashcards.core.ports.services.CreateFlashcardPort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CreateFlashcard implements CreateFlashcardPort {
    private FlashcardRepositoryPort flashcardRepository;
    private Flashcard entity;

    public CreateFlashcard(FlashcardRepositoryPort flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }

    @Override
    public Map<String, String> createFlashcard(CreateFlashcardDTO createFlashcardDTO) {
        Flashcard entity = new Flashcard(
                createFlashcardDTO.userId(),
                createFlashcardDTO.textQuestion(),
                createFlashcardDTO.urlQuestion(),
                createFlashcardDTO.answer()
        );

        flashcardRepository.create(entity);

        Map<String, String> result = new HashMap<>();
        result.put("id", entity.getId().toString());
        result.put("userId", entity.getUserId().toString());
        result.put("testQuestion", entity.getTextQuestion());
        result.put("urlQuestion", entity.getUrlQuestion());
        result.put("answer", entity.getAnswer());

        return result;
    }
}
