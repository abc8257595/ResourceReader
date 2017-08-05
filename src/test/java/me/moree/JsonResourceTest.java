package me.moree;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import me.moree.format.ResourceFactory;
import me.moree.format.StringResource;
import me.moree.util.JsonUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by MORE-E on 3/27/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsonResourceTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void getTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("albums.json");
        String title = res.get("/title");
        logger.info(title);
        Assert.assertEquals("Free Music Archive - Albums", title);

        String emptyArray = res.get("/errors");
        Assert.assertEquals("[]", emptyArray);

        String dataSet0 = res.get("/dataset/0");
        logger.info(dataSet0);

        // json to object
        DataSet dataSet = JsonUtils.json2bean(dataSet0, DataSet.class);
        Assert.assertNotNull(dataSet);
        Assert.assertEquals("!!! - Live @ KEXP 7/24/2010", dataSet.getTitle());
        Assert.assertEquals(new Integer(7596), dataSet.getId());
        Assert.assertEquals("[tag1]", dataSet.getTags().toString());

        String dataSets = res.get("/dataset");
        // json to list
        List<DataSet> dataSetList = JsonUtils.json2list(dataSets, DataSet.class);
        long num = 5;
        Assert.assertEquals(num, dataSetList.size());
    }

    @Test
    public void setTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("albums.json");
        logger.info(res.set("/title", "test"));
        Assert.assertEquals("test", res.get("/title"));
        Assert.assertTrue(res.save());

        // reset
        res.set("/title", "Free Music Archive - Albums");
        Assert.assertTrue(res.save());
    }

    @Test
    public void removeTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("albums.json");
        logger.info(res.remove("/title"));
        Assert.assertNull(res.get("/title"));
        Assert.assertTrue(res.save());

        // reset
        res.set("/title", "Free Music Archive - Albums");
        Assert.assertTrue(res.save());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class DataSet {
        @JsonProperty(value = "album_title")
        private String title;
        @JsonProperty(value = "album_id")
        private Integer id;
        @JsonProperty(value = "tags")
        private List<String> tags;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}
