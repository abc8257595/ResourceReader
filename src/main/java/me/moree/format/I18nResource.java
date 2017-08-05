package me.moree.format;

import me.moree.ResourceContract;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * Created by MORE-E on 3/20/2017.
 */
public class I18nResource extends PropertyResource {

    @Override
    void parse(final Class<?> baseClass, String dirPath, final String i18nFileName) throws IOException {
        if (dirPath == null) {
            dirPath = ResourceContract.getDirList(i18nFileName, null).get(0);
        }
        String content = load(baseClass, dirPath, i18nFileName);
        Properties properties = new Properties();
        properties.load(new StringReader(content));
        propertiesList.add(properties);

        // parse other i18n files
        I18nType[] types = I18nType.values();
        String currentType = ResourceContract.getI18nType(i18nFileName);
        for (I18nType type : types) {
            if (type.toString().equals(currentType)) {
                continue;
            }
            String otherI18nFileName = String.format(ResourceContract.I18N_FILENAME, type.toString());
            content = load(baseClass, dirPath, otherI18nFileName);
            properties = new Properties();
            properties.load(new StringReader(content));
            propertiesList.add(properties);
        }
    }

    @Override
    // It is forbidden to modify i18n files on fly.
    public boolean save() throws IOException {
        return false;
    }
}
