package me.moree.format;

import me.moree.ResourceContract;
import me.moree.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by MORE-E on 3/15/2017.
 */
public class PropertyResource extends StringResource {
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected Class<?> baseClass;
    protected List<String> dirList = new ArrayList<>();
    protected String fileName;

    protected List<Properties> propertiesList = new ArrayList<>();

    @Override
    void parse(final Class<?> baseClass, final String dirPath, final String fileName) throws IOException {
        this.baseClass = baseClass;
        this.fileName = fileName;
        if (dirPath != null) {
            dirList.add(dirPath);
            String content = load(baseClass, dirPath, fileName);
            Properties properties = new Properties();
            properties.load(new StringReader(content));
            propertiesList.add(properties);
        } else {
            dirList = ResourceContract.getDirList(fileName, dirList);
            Iterator<String> iterator = dirList.iterator();
            while (iterator.hasNext()) {
                try {
                    String content = load(baseClass, iterator.next(), fileName);
                    Properties properties = new Properties();
                    properties.load(new StringReader(content));
                    propertiesList.add(properties);
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
    public String get(String key) {
        if (ResourceUtils.isCollectionEmpty(propertiesList)) {
            logger.warn("propertiesList contains nothing! Fail to get.");
            return null;
        }
        String value = null;
        for (Properties properties : propertiesList) {
            String tmp = properties.getProperty(key);
            if (!ResourceUtils.isNull(tmp)) {
                value = tmp;
            }
            if (!ResourceUtils.isStringEmpty(value))
                break;
        }
        return value;
    }

    @Override
    public String set(String key, String value) {
        if (ResourceUtils.isCollectionEmpty(propertiesList)) {
            logger.warn("propertiesList contains nothing! Fail to set.");
            return null;
        }
        Object preValue = propertiesList.get(0).setProperty(key, value);
        return (String) preValue;
    }

    @Override
    public String add(String key, String value) {
        return set(key, value);
    }

    @Override
    public String remove(String key) {
        if (ResourceUtils.isCollectionEmpty(propertiesList)) {
            logger.warn("propertiesList contains nothing! Fail to remove.");
            return null;
        }
        String value = null;
        for (Properties properties : propertiesList) {
            Object obj = properties.remove(key);
            if (obj != null) {
                value = (String) obj;
                break;
            }
        }
        return value;
    }

    public boolean save() throws IOException {
        if (ResourceUtils.isCollectionEmpty(propertiesList)) {
            logger.warn("propertiesList contains nothing! Fail to save.");
            return true;
        }
        int index = 0;
        for (Properties properties : propertiesList) {
            StringWriter sw = new StringWriter();
            properties.store(sw, null);
            if (save(baseClass, dirList.get(index), fileName, sw.toString())) {
                return true;
            }
            index++;
        }

        return false;
    }
}
