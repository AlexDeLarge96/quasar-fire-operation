package com.quasar.operation.services.impl;

import com.quasar.operation.exceptions.RequestException;
import com.quasar.operation.services.MessageDecryptionService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class MessageDecryptionServiceImpl implements MessageDecryptionService {

    @Override
    public String decryptMessage(@NotNull List<List<String>> messages) {
        Map<String, Double> differentWords = new HashMap<>();
        Set<Integer> messagesLengths = new HashSet<>();
        for (List<String> message : messages) {
            messagesLengths.add(message.size());
            updateDifferentWords(message, differentWords);
        }

        int totalDifferentWords = differentWords.keySet().size();
        boolean messageCanBeDecrypted = messagesLengths.size() == 1 && messagesLengths.contains(totalDifferentWords);
        if (messageCanBeDecrypted) {
            return decryptMessage(differentWords);
        } else {
            throw new RequestException(NOT_FOUND, "The message cannot be determined.");
        }
    }

    private void updateDifferentWords(@NotNull List<String> message, @NotNull Map<String, Double> differentWords) {
        for (int i = 0; i < message.size(); i++) {
            String word = message.get(i);
            if (!word.isEmpty()) {
                double currentWordPosition = differentWords.containsKey(word) ? differentWords.get(word) : 0;
                double averageWordPosition = currentWordPosition > 0 ? (i + 1 + currentWordPosition) / 2 : i + 1;
                differentWords.put(word, averageWordPosition);
            }
        }
    }

    private String decryptMessage(@NotNull Map<String, Double> differentWords) {
        return differentWords
                .entrySet()
                .stream()
                .sorted(comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(joining(" "));
    }
}
