package me.moree;

import me.moree.format.I18nType;
import me.moree.format.ResourceFactory;
import me.moree.format.StringResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by MORE-E on 3/24/2017.
 */
public class I18nResourceTest {

    @Before
    public void setL() {
        ResourceFactory.setL(I18nType.EN);
    }

    @Test
    public void getTest() {
        StringResource L = ResourceFactory.getL();
        String title = L.get("app.title");
        Assert.assertEquals(title, "Title");

        String file = L.get("ui.menu.file");
        Assert.assertEquals(file, "File");

        String reserve = L.get("ui.reserve");
        Assert.assertEquals(reserve, "预留");

        String nullStr = L.get("null");
        Assert.assertNull(nullStr);
    }
}
