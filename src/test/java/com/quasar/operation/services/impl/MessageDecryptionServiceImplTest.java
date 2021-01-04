package com.quasar.operation.services.impl;

import com.quasar.operation.exceptions.RequestException;
import com.quasar.operation.services.MessageDecryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageDecryptionServiceImplTest {

    private MessageDecryptionService messageDecryptionService;

    @BeforeEach
    public void init() {
        messageDecryptionService = new MessageDecryptionServiceImpl();
    }


    @Test
    void shouldDecryptMessage() {
        List<String> message1 = Arrays.asList("this", "", "", "secret", "");
        List<String> message2 = Arrays.asList("", "is", "", "", "message");
        List<String> message3 = Arrays.asList("this", "", "a", "", "");
        List<List<String>> messages = Arrays.asList(message1, message2, message3);
        String secretMessage = messageDecryptionService.decryptMessage(messages);

        assertEquals("this is a secret message", secretMessage);
    }

    @Test
    void shouldThrowExceptionBecauseMsgCanBeDecrypted() {
        List<String> message1 = Arrays.asList("this", "", "", "secret", "");
        List<String> message2 = Arrays.asList("", "");
        List<List<String>> messages = Arrays.asList(message1, message2);
        boolean testPassed = false;

        try {
            messageDecryptionService.decryptMessage(messages);
        } catch (RequestException ignored) {
            testPassed = true;
        }

        assertTrue(testPassed);
    }
}