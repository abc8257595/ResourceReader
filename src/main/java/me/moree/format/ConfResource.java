package me.moree.format;

import me.moree.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by MORE-E on 3/16/2017.
 */
public class ConfResource extends PropertyResource {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String PLACEHOLDER_START = "${";

    private List<Map<String, String>> resolvedMapList = new ArrayList<>();

    @Override
    void parse(final Class<?> baseClass, final String dirPath, final String fileName) throws IOException {
        super.parse(baseClass, dirPath, fileName);
        resolve();
    }

    private void resolve() {
        int len = propertiesList.size();
        resolvedMapList.clear();
        for (Properties properties : propertiesList) {
            Map<String, String> map = new HashMap<>();
            resolveOne(properties, map);
            resolvedMapList.add(map);
        }
    }

    private void resolveOne (Properties properties, Map<String, String> map) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            final String key = (String) entry.getKey();
            final String toBeResolved = (String) entry.getValue();
            String resolved = resolvePlaceHolder(toBeResolved, properties, map);
            if (null == resolved) {
                logger.info("Can't get resolve value for key: " + key);
            } else {
                map.put(key, resolved);
            }
        }
    }

    @Override
    public String get(String key) {
        if (ResourceUtils.isCollectionEmpty(resolvedMapList)) {
            logger.warn("ConfResource contains nothing!");
            return null;
        }
        String value = null;
        for (Map<String, String> map : resolvedMapList) {
            value = map.get(key);
            if (!ResourceUtils.isStringEmpty(value))
                break;
        }
        return value;
    }

    @Override
    public String set(String key, String value) {
        String setValue = super.set(key, value);
        if (!ResourceUtils.isNull(setValue)) {
            String setValueInternal = get(key);
            resolve();
            return setValueInternal;
        }
        return null;
    }

    @Override
    public String add(String key, String value) {
        return set(key, value);
    }

    @Override
    public String remove(String key) {
        String removeValue = super.remove(key);
        if (!ResourceUtils.isNull(removeValue)) {
            String valueInternal = get(key);
            resolve();
            return valueInternal;
        }
        return null;
    }

    private String resolvePlaceHolder (String toBeResolved, Properties properties, Map<String, String> resolvedMap) {
        if (toBeResolved.indexOf(PLACEHOLDER_START) < 0) {
            return toBeResolved;
        }
        StringBuilder buff = new StringBuilder();
        char[] chars = toBeResolved.toCharArray();
        for (int pos = 0; pos < chars.length; pos++) {
            if (chars[pos] == '$') {
                if (chars[pos + 1] == '{') {
                    String resolvedKey = "";
                    int x = pos + 2;
                    for (; x < chars.length && chars[x] != '}'; x++) {
                        resolvedKey += chars[x];
                        if (x == chars.length - 1) {
                            throw new IllegalArgumentException("unmatched placeholder start ["
                                    + toBeResolved + "]");
                        }
                    }
                    String resolvedValue = resolvedMap.get(resolvedKey);
                    if (null == resolvedValue) {
                        if (null != System.getProperty(resolvedKey)) {
                            resolvedValue = System.getProperty(resolvedKey);
                        } else {
                            String parentOriginalValue = (String) properties.get(resolvedKey);
                            if (null == parentOriginalValue) {
                                return null;
                            } else {
                                resolvedValue = resolvePlaceHolder(parentOriginalValue, properties, resolvedMap);
                            }
                        }
                    }
                    buff.append(resolvedValue);
                    pos = x + 1;
                    if (pos >= chars.length) {
                        break;
                    }
                }
            }
            buff.append(chars[pos]);
        }

        return buff.toString();
    }
}
