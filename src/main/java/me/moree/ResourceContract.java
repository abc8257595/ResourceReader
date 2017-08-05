package me.moree;

import me.moree.format.ResourceFormat;
import me.moree.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MORE-E on 3/15/2017.
 */
public final class ResourceContract {
    // Directory
    public static final char SEPARATOR           = '/';
    private static final String ETC_DIR          = "/etc/";
    private static final String ETC_USER_DIR     = "/etc/user/";
    private static final String I18N_DIR         = "/i18n/";
    private static final String RES_DIR          = "/res/";

    // File
    public static final String DEFAULT_FILE     = "default.properties";

    // I18N
    private static final String I18N_PATTERN_STR = "i18n_(\\w{2,5}).properties";
    public static final String I18N_FILENAME     = "i18n_%s.properties";
    private static final Pattern I18N_PATTERN    = Pattern.compile(I18N_PATTERN_STR);

    public static List<String> getDirList(String fileName, List<String> dirList) {
        if (ResourceUtils.isStringEmpty(fileName)) {
            fileName = DEFAULT_FILE;
        }
        if (dirList == null) {
            dirList = new ArrayList<String>();
        }
        if (isI18nFile(fileName)) {
            dirList.add(I18N_DIR);
        } else {
            switch (ResourceFormat.parseFormat(fileName)) {
                case CONF:
                case JSON:
                case PROPERTY:
                case XML: {
                    dirList.add(ETC_USER_DIR);
                    dirList.add(ETC_DIR);
                    break;
                }
                case OTHER:
                default: dirList.add(RES_DIR);
            }
        }
        return dirList;
    }

    public static boolean isI18nFile(String fileName) {
        Matcher matcher = I18N_PATTERN.matcher(fileName);
        return matcher.matches();
    }

    public static String getI18nType(String i18nFileName) {
        Matcher matcher = I18N_PATTERN.matcher(i18nFileName);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
