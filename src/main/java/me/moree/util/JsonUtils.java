package me.moree.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by MORE-E on 3/23/2017.
 */
public class JsonUtils {
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper mapper = new ObjectMapper();

    // mapper configuration
    static {
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    @SuppressWarnings("unchecked")
    public static <T> T json2bean(String json, Class<?> T) {
        try {
            return (T) mapper.readValue(json, T);
        } catch (IOException e) {
            logger.warn("Parse json message exception .", e);
        }
        return null;
    }

    public static <T> List<T> json2list(String json, Class<?> T) {
        JavaType type = getCollectionType(List.class, T);
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.warn("Fail to transfer json to list .", e);
        }
        return null;
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?> elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
