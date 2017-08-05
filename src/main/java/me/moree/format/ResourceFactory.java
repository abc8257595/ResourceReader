package me.moree.format;

import me.moree.Resource;
import me.moree.ResourceContract;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by MORE-E on 3/16/2017.
 */
public class ResourceFactory {
    private static StringResource L;

    static {
        // parse i18n_{system language}.properties
        String i18nFileName = String.format(ResourceContract.I18N_FILENAME, Locale.getDefault().getLanguage());
        try {
            L = genStringResource(i18nFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StringResource genStringResource(String fileName) throws IOException {
        return genStringResource(null, fileName);
    }

    public static StringResource genStringResource(String path, String fileName) throws IOException {
        return genStringResource(null, path, fileName);
    }

    public static StringResource genStringResource(Class<?> baseClass, String dirPath, String fileName) throws IOException {
        StringResource resource = null;
        switch (ResourceFormat.parseFormat(fileName)) {
            case I18N: {
                resource = new I18nResource();
                break;
            }
            case CONF: {
                resource = new ConfResource();
                break;
            }
            case PROPERTY: {
                resource = new PropertyResource();
                break;
            }
            case JSON: {
                resource = new JsonResource(ResourceFormat.JSON);
                break;
            }
            case XML: {
                resource = new JsonResource(ResourceFormat.XML);
                break;
            }
        }
        if (resource != null) {
            resource.parse(baseClass, dirPath, fileName);
        }
        return resource;
    }

    public static void setL(I18nType type) {
        try {
            String i18nFileName = String.format(ResourceContract.I18N_FILENAME, type.toString());
            L = genStringResource(i18nFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StringResource getL() {
        return L;
    }

    public static Resource<Object, Object> getResource(String fileName) {
        return null;
    }

    public static Resource<Object, Object> getResource(String path, String fileName) {
        return null;
    }

}
