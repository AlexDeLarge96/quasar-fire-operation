package com.quasar.operation.utils;

import com.quasar.operation.domain.Position;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class QuasarUtilsTest {

    @Test
    void getObjectFromJson() {
        Optional<Position> position = QuasarUtils.getObjectFromJson("{\"x\":100,\"y\":120}", Position.class);
        assertTrue(position.isPresent());
        assertEquals(100, position.get().getX());
        assertEquals(120, position.get().getY());
    }

    @Test
    void getObjectFromJsonReturnEmpty() {
        Optional<Position> position = QuasarUtils.getObjectFromJson("{\"", Position.class);
        assertFalse(position.isPresent());
    }

    @Test
    void objectToJson() {
        String json = QuasarUtils.objectToJson(new Position(10, 20));
        assertEquals("{\"x\":10.0,\"y\":20.0}", json);
    }

    @Test
    void base64Decode() {
        String decoded = QuasarUtils.base64Decode("dGVzdFBhc3NlZA==");
        assertEquals("testPassed", decoded);
    }

    @Test
    void base64DecodeReturnsEmptyString() {
        String decoded = QuasarUtils.base64Decode("4rdHFh%2BHYoS8oLdVvbUzEVqB8Lvm7kSPnuwF0AAABYQ%3D");
        assertTrue(decoded.isEmpty());
    }

    @Test
    void base64Encode() {
        String encoded = QuasarUtils.base64Encode("testPassed");
        assertEquals("dGVzdFBhc3NlZA==", encoded);
    }
}