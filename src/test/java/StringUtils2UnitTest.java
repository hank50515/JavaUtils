import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class StringUtils2UnitTest {

    @Test
    public void containsAnyIgnoreCase(){
        List<String> keywords = Lists.newArrayList("a","b","c","D");

        Assert.assertTrue(StringUtils2.containsAnyIgnoreCase("a", keywords));
        Assert.assertTrue(StringUtils2.containsAnyIgnoreCase("A", keywords));
        Assert.assertTrue(StringUtils2.containsAnyIgnoreCase("d", keywords));
        // False
        Assert.assertFalse(StringUtils2.containsAnyIgnoreCase("f", keywords));
    }

    @Test
    public void containsAny(){
        List<String> keywords = Lists.newArrayList("a","b","c","D");

        Assert.assertTrue(StringUtils2.containsAny("a", keywords));
        // False
        Assert.assertFalse(StringUtils2.containsAny("A", keywords));
        Assert.assertFalse(StringUtils2.containsAny("d", keywords));
        Assert.assertFalse(StringUtils2.containsAny("f", keywords));
    }
}
