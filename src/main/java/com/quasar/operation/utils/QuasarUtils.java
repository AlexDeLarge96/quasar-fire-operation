package com.quasar.operation.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.springframework.lang.NonNull;

import java.util.Base64;
import java.util.Optional;

public class QuasarUtils {

    private QuasarUtils() {
    }

    public static <T> Optional<T> getObjectFromJson(@NonNull String json, @NonNull Class<T> objectClass) {
        try {
            Gson gson = new Gson();
            return Optional.ofNullable(gson.fromJson(json, objectClass));
        } catch (JsonParseException e) {
            return Optional.empty();
        }
    }

    public static String objectToJson(Object object) {
        try {
            return new Gson().toJson(object);
        } catch (JsonParseException e) {
            return "{}";
        }
    }

    public static String base64Decode(@NonNull String text) {
        try {
            return new String(Base64.getDecoder().decode(text));
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    public static String base64Encode(@NonNull String text) {
        try {
            return Base64.getEncoder().encodeToString(text.getBytes());
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

}
