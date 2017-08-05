package me.moree;

import me.moree.format.PropertyResource;
import me.moree.format.StringResource;
import me.moree.util.ResourceUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by MORE-E on 3/19/2017.
 */
public class StringResourceTest {
    @Test
    public void getDirListTest() {
        List<String> dirs = null;
        dirs = ResourceContract.getDirList("test.conf", dirs);
        System.out.println(dirs);
        dirs = ResourceContract.getDirList("test.img", dirs);
        System.out.println(dirs);
        dirs = ResourceContract.getDirList("i18n_zh.properties", dirs);
        System.out.println(dirs);
    }

    @Test
    public void loadStringResourceTest() throws IOException {
        StringResource res = new PropertyResource();
        String str = res.load("test.conf");
        System.out.println(str);
    }

    @Test
    public void loadStringResourceTest2() {
        String str = ResourceUtils.load("/etc/user/","test.conf");
        System.out.println(str);
    }
}
