package com.demo.uploads


import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

class TestObjectMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper()

    TestObjectMapper() {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.findAndRegisterModules()
    }

    static <T> T fromJson(String json, Class<T> clazz) {
        objectMapper.readValue(json, clazz)
    }

    static String asJsonString(final obj) {
        try {
            return objectMapper.writeValueAsString(obj)
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }
}
