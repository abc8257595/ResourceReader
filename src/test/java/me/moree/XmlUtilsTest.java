package me.moree;

import me.moree.util.ResourceUtils;
import me.moree.util.XmlUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by MORE-E on 5/10/2017.
 */
public class XmlUtilsTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void xml2JsonTest() {
        String xml = ResourceUtils.load("/etc/","test.xml");
        logger.info(xml);
        String json = XmlUtils.xml2Json(xml);
        logger.info(json);
    }

    @Test
    public void json2XmlTest() {
        String xml = ResourceUtils.load("/etc/","test.xml");
        String json = XmlUtils.xml2Json(xml);
        logger.info(json);
        String convertXml = XmlUtils.json2Xml(json);
        logger.info(convertXml);
    }
}
