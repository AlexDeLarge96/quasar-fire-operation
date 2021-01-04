package com.quasar.operation.config;

import com.google.gson.*;
import com.quasar.operation.utils.annotations.Exclude;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Type;

@Configuration
public class JSONParserConfig {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Gson getJSONParser() {
        ExclusionStrategy strategy = new CustomExclusionStrategy();
        return new GsonBuilder()
                .setExclusionStrategies(strategy)
                .registerTypeAdapter(Json.class, new SpringFoxJsonAdapter())
                .setDateFormat(DATE_FORMAT)
                .create();
    }

    static class CustomExclusionStrategy implements ExclusionStrategy {

        public static final String THROWABLE = "java.lang.Throwable";

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes field) {
            String declaringClass = field.getDeclaringClass().getName();
            if (declaringClass.equals(THROWABLE)) {
                return true;
            } else {
                return field.getAnnotation(Exclude.class) != null;
            }
        }
    }

    private static class SpringFoxJsonAdapter implements JsonSerializer<Json> {
        @Override
        public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
            return JsonParser.parseString(json.value());
        }
    }
}
