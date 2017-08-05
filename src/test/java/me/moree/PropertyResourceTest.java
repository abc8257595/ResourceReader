package me.moree;

import me.moree.format.ResourceFactory;
import me.moree.format.StringResource;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

/**
 * Created by MORE-E on 3/24/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PropertyResourceTest {

    @Test
    public void getTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("info.properties");
        String name = res.get("name");
        Assert.assertEquals(name, "MORE-E");

        res = ResourceFactory.genStringResource("/etc/user/", "info.properties");
        String gender = res.get("gender");
        Assert.assertEquals(gender, "male");

        res = ResourceFactory.genStringResource("/etc/user", "info.properties");
        String gender2 = res.get("gender");
        Assert.assertEquals(gender2, "male");

        res = ResourceFactory.genStringResource("/etc/user/", "info.properties");
        String job = res.get("job");
        Assert.assertNull(job);

        res = ResourceFactory.genStringResource("info.properties");
        job = res.get("job");
        Assert.assertEquals(job, "developer");
    }

    @Test
    public void setTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("info.properties");
        String age = res.get("age");
        Assert.assertEquals(age, "24");

        String prev = res.set("age", "25");
        Assert.assertEquals(prev, "24");
        age = res.get("age");
        Assert.assertEquals(age, "25");
    }

    @Test
    public void saveTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("info.properties");
        res.set("age", "27");
        String job = res.get("age");
        Assert.assertTrue(res.save());
        Assert.assertEquals(res.get("age"), "27");

        // reset
        res.set("age", "24");
        Assert.assertTrue(res.save());
    }

    @Test
    public void addTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("info.properties");
        String hobby = res.get("hobby");
        Assert.assertNull(hobby);

        String addResult = res.add("hobby", "travelling");
        hobby = res.get("hobby");
        Assert.assertEquals(hobby, "travelling");

        res.save();

        StringResource newRes = ResourceFactory.genStringResource("info.properties");
        String newHobby = newRes.get("hobby");
        Assert.assertEquals(newHobby, "travelling");
    }

    @Test
    public void removeTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("info.properties");
        String bobby = res.get("hobby");
        Assert.assertEquals(bobby, "travelling");

        res.remove("hobby");

        Assert.assertTrue(res.save());

        StringResource newRes = ResourceFactory.genStringResource("info.properties");
        String newHobby = newRes.get("hobby");
        Assert.assertNull(newHobby);
    }

    @Test
    public void emptyStrTest() throws IOException {
        StringResource res = ResourceFactory.genStringResource("info.properties");
        String empty = res.get("empty");
        Assert.assertNotNull(empty);
        Assert.assertEquals(empty, "");
    }
}
