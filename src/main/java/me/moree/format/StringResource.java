package me.moree.format;

import me.moree.Resource;
import me.moree.ResourceContract;
import me.moree.exception.FileTooLargeException;
import me.moree.util.ResourceUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MORE-E on 3/16/2017.
 */
abstract public class StringResource implements Resource<String, String> {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4; // 4K

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if no mapping for the key
     */
    abstract public String get(String key);

    /**
     * set value corresponding to <tt>key</tt>
     * @param key the key to be placed into internal structure
     * @param value the value corresponding to <tt>key</tt>
     * @return     the previous value of the specified key, or {@code null} if it did not have one.
     */
    abstract public String set(String key, String value);

    abstract public String add(String key, String value);

    /**
     * remove an entry if the key have a mapping
     * @param key the key to be removed
     * @return the value corresponding to the <tt>key</tt>, or {@code null} if the key did not have a mapping
     */
    abstract public String remove(String key);

    abstract public boolean save() throws IOException;

    abstract void parse(Class<?> baseClass, String dirPath, String fileName) throws IOException;

    boolean save(Class<?> baseClass, String dirPath, String fileName, String content) throws IOException {
        if (baseClass == null) {
            baseClass = System.class;
        }
        if (ResourceUtils.isStringEmpty(fileName)) {
            fileName = ResourceContract.DEFAULT_FILE;
        }
        List<String> dirList = new ArrayList<String>();
        if (ResourceUtils.isStringEmpty(dirPath)) {
            dirPath = ResourceContract.getDirList(fileName, dirList).get(0);
        }

        URL url = baseClass.getResource(dirPath + fileName);
        if (url == null) {
            throw new FileNotFoundException();
        }
        try {
            File file = new File(url.toURI());
            if (file.isFile() && file.canWrite()) {
                try (Writer out = new FileWriter(file)) {
                    out.write(content);
                    return true;
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }


    public String load(String fileName) throws IOException {
        return load(null, fileName);
    }

    public String load(String path, String fileName) throws IOException {
        return load(null, path, fileName);
    }

    public String load(Class<?> baseClass, String dirPath, String fileName) throws IOException {
        if (baseClass == null) {
            baseClass = System.class;
        }
        if (ResourceUtils.isStringEmpty(fileName)) {
            fileName = ResourceContract.DEFAULT_FILE;
        }
        List<String> dirList = new ArrayList<String>();
        if (ResourceUtils.isStringEmpty(dirPath)) {
            dirPath = ResourceContract.getDirList(fileName, dirList).get(0);
        }

        if (dirPath.charAt(dirPath.length() - 1) != ResourceContract.SEPARATOR) {
            dirPath = dirPath + ResourceContract.SEPARATOR;
        }

        try (InputStream is = baseClass.getResourceAsStream(dirPath + fileName)) {
            if (is != null) {
                InputStreamReader in = new InputStreamReader(is);
                StringWriter out = new StringWriter();
                long count = copyLarge(in, out);
                if (count > Integer.MAX_VALUE) {
                    throw new FileTooLargeException();
                }
                return out.toString();
            } else {
                throw new FileNotFoundException();
            }
        }
    }

    private static long copyLarge(Reader input, Writer output) throws IOException {
        char[] buffer = new char[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
