package me.moree;

import me.moree.format.ResourceFactory;
import me.moree.format.StringResource;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by MORE-E on 3/24/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfResourceTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void getTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("test.conf");
        String name = res.get("name");
        logger.info(name);

        String motto = res.get("motto");
        logger.info(motto);

        String motto2 = res.get("motto2");
        logger.info(motto2);

        String motto1 = res.get("motto1");
        logger.info(motto1);
    }

    @Test
    public void setTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("test.conf");
        String name = res.get("name");
        Assert.assertEquals(name, "MORE-E");

        String prev = res.set("name", "haha");
        Assert.assertEquals(prev, "MORE-E");
        name = res.get("name");
        Assert.assertEquals(name, "haha");
        String motto = res.get("motto");
        logger.info(motto);

        Assert.assertTrue(res.save());

        // reset
        res.set("name", "MORE-E");
        Assert.assertTrue(res.save());
    }

    @Test
    public void setTest2() throws IOException {
        StringResource res = ResourceFactory.genStringResource("test.conf");
        logger.info(res.set("name", "test"));

        String motto = res.get("motto");
        logger.info(motto);

        logger.info(res.set("motto", "${name} ${name}"));
        motto = res.get("motto");
        logger.info(motto);

        logger.info("save:" + res.save());

        // reset
        res.set("motto", "${name} is great!");
        res.set("name", "MORE-E");
        logger.info("save:" + res.save());
    }

    @Test
    public void removeTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("test.conf");
        String value = res.remove("motto");
        logger.info(value);
        Assert.assertNull(res.get("motto"));
    }

}
