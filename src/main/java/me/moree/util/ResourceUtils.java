package me.moree.util;

import me.moree.exception.EmptyFileNameException;
import me.moree.format.PropertyResource;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by MORE-E on 3/15/2017.
 */
public class ResourceUtils {
    public static File getFile (String filePath) {
        File file = new File(filePath);
        if (file != null && file.exists())
            return file;
        file = new File(filePath);
        if (file == null) {

        }
        return (null == file || !file.exists()) ? null : file;
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isStringEmpty(String s) {
        return s == null || "".equals(s);
    }

    public static boolean isCollectionEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static String load(String path, String fileName) {
        try {
            return new PropertyResource().load(path, fileName);
        } catch (IOException e) {
            throw new EmptyFileNameException();
        }
    }
}
