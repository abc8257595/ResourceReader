package me.moree.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by MORE-E on 5/9/2017.
 */
public class XmlUtils {
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static XmlMapper mapper = new XmlMapper();

    // mapper configuration
    static {
    }

    public static XmlMapper getMapper() {
        return mapper;
    }

    @SuppressWarnings("unchecked")
    public static <T> T xml2Bean(String xml, Class<?> T) {
        try {
            return (T) mapper.readValue(xml, T);
        } catch (IOException e) {
            logger.warn("Parse xml message exception .", e);
        }
        return null;
    }

    public static String bean2Xml(Object bean) {
        try {
            return mapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            logger.warn("Parse xml message exception .", e);
        }
        return null;
    }

    public static String xml2Json(String xml) {
        return XML.toJSONObject(xml).toString();
    }

    public static String json2Xml(String json) {
        JSONObject jsonObject = new JSONObject(json);
        return XML.toString(jsonObject);
    }
}
