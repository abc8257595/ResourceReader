package me.moree.format;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.moree.ResourceContract;
import me.moree.util.JsonUtils;
import me.moree.util.ResourceUtils;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by MORE-E on 3/15/2017.
 */
public class JsonResource extends StringResource {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Class<?> baseClass;
    private List<String> dirList = new ArrayList<>();
    private String fileName;
    private ObjectMapper mapper = JsonUtils.getMapper();
    private ResourceFormat format = ResourceFormat.JSON;

    private List<JsonNode> jsonNodeList = new ArrayList<>();

    public JsonResource() {}

    public JsonResource(ResourceFormat format) {
        if (format == ResourceFormat.JSON || format == ResourceFormat.XML) {
            this.format = format;
        } else {
            throw new IllegalArgumentException("Only accept json or xml.");
        }
    }

    @Override
    void parse(final Class<?> baseClass, final String dirPath, final String fileName) throws IOException {
        this.baseClass = baseClass;
        this.fileName = fileName;
        if (dirPath != null) {
            dirList.add(dirPath);
            String content = load(baseClass, dirPath, fileName);
            JsonNode node = mapper.readTree(content);
            jsonNodeList.add(node);
        } else {
            dirList = ResourceContract.getDirList(fileName, dirList);
            Iterator<String> iterator = dirList.iterator();
            while (iterator.hasNext()) {
                try {
                    String content = load(baseClass, iterator.next(), fileName);
                    if (format == ResourceFormat.XML) {
                        content = XML.toJSONObject(content).toString();
                    }
                    JsonNode node = mapper.readTree(content);
                    jsonNodeList.add(node);
                } catch (IOException e) {
                    iterator.remove();
                }
            }
            if (ResourceUtils.isCollectionEmpty(dirList)) {
                throw new FileNotFoundException();
            }
        }
    }

    @Override
    /**
     * JsonPtr format reference: https://tools.ietf.org/html/draft-ietf-appsawg-json-pointer-03
     */
    public String get(String jsonPtrExpr) {
        String value = null;
        for (JsonNode node : jsonNodeList) {
            JsonNode at = node.at(jsonPtrExpr);
            if (!at.isMissingNode()) {
                if (at.isValueNode()) {
                    value = at.asText();
                } else {
                    value = at.toString();
                }
                break;
            }
        }
        if ("null".equals(value)) {
            return null;
        }
        return value;
    }

    @Override
    public String set(String jsonPtrExpr, String value) {
        if (ResourceUtils.isStringEmpty(jsonPtrExpr)) {
            throw new IllegalArgumentException("jsonPtrExpr can not be empty.");
        }
        String prevValue = get(jsonPtrExpr);
        int slashIndex = jsonPtrExpr.lastIndexOf(ResourceContract.SEPARATOR);
        if (slashIndex != -1) {
            String ptr = jsonPtrExpr.substring(0, slashIndex);
            String key = jsonPtrExpr.substring(slashIndex + 1, jsonPtrExpr.length());
            for (JsonNode node : jsonNodeList) {
                JsonNode at = node.at(ptr);
                if (!at.isMissingNode()) {
                    ((ObjectNode) at).put(key, value);
                    return prevValue;
                }
            }
        }
        return null;
    }

    @Override
    /**
     * Not Supported yet.
     */
    public String add(String jsonPtrExpr, String value) {
        return null;
    }

    @Override
    /**
     * equal to set(jsonPtrExpr, null)
     */
    public String remove(String jsonPtrExpr) {
        return set(jsonPtrExpr, null);
    }

    @Override
    /**
     * Xml can not be saved yet.
     */
    public boolean save() throws IOException {
        if (format == ResourceFormat.XML) {
            return false;
        }
        if (ResourceUtils.isCollectionEmpty(jsonNodeList)) {
            logger.warn("JsonResource contains nothing!");
            return true;
        }
        int index = 0;
        for (JsonNode jsonNode : jsonNodeList) {
            String toWrite = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            if (save(baseClass, dirList.get(index), fileName, toWrite)) {
                return true;
            }
            index++;
        }
        return false;
    }
}
