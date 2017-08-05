package me.moree;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MORE-E on 3/16/2017.
 */
public class UtilTest {

    @Test
    public void regTest() {
        String I18N_PATTERN     = "i18n_(\\w{2,5}).properties";
        String ZH               = "zh";
        String EN               = "en";

        String fileName = "i18n_zh.properties";
        Pattern pattern = Pattern.compile(I18N_PATTERN);
        Matcher matcher = pattern.matcher(fileName);
        System.out.println(matcher.matches());
        String custom = matcher.group(1);
        System.out.println(custom);

//        if (matcher.find()) {
//            String custom = matcher.group(1);
//            System.out.println(custom);
//        }
    }

}
