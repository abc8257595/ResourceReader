package me.moree;

import me.moree.format.ResourceFactory;
import me.moree.format.StringResource;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by MORE-E on 5/9/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class XmlResourceTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void getTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("test.xml");
        String menu = res.get("/ui/menus/menu/0");
        logger.info(menu);

        String name = res.get("/ui/menus/menu/0/subMenus/menu/0/name");
        logger.info(name);

        String imgUrl = res.get("/ui/toolbar/tool/0/imgUrl");
        logger.info(imgUrl);
    }

}
