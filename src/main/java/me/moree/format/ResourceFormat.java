package me.moree.format;

import me.moree.ResourceContract;
import me.moree.exception.EmptyFileNameException;
import me.moree.util.ResourceUtils;

/**
 * Created by MORE-E on 3/16/2017.
 */
public enum ResourceFormat {
    CONF("conf"),
    JSON("json"),
    PROPERTY("properties"),
    I18N("properties"),
    XML("xml"),
    OTHER("*");

    private static final char EXTENSION_SEPARATOR = '.';
    private String extension;

    ResourceFormat(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static ResourceFormat parseFormat(String fileName) {
        if (ResourceUtils.isStringEmpty(fileName)) {
            throw new EmptyFileNameException();
        }
        int extensionPos = fileName.lastIndexOf(EXTENSION_SEPARATOR);
        if (extensionPos == -1) {
            throw new ExtensionNotFoundException();
        }

        if (ResourceContract.isI18nFile(fileName)) {
            return I18N;
        }

        String extension = fileName.substring(extensionPos + 1);
        if (extension.equals(CONF.getExtension())) {
            return CONF;
        } else if (extension.equals(JSON.getExtension())) {
            return JSON;
        } else if (extension.equals(PROPERTY.getExtension())) {
            return PROPERTY;
        } else if (extension.equals(XML.getExtension())) {
            return XML;
        } else {
            return OTHER;
        }
    }

    public static class ExtensionNotFoundException extends RuntimeException {
        ExtensionNotFoundException() {
            super("Fail to find valid extension.");
        }
    }

    public static class UnsupportedFormatException extends RuntimeException {
        UnsupportedFormatException() {
            super("Extension format is unsupported.");
        }
    }

}
