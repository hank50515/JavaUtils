import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class AbsolutePathUtilsUnitTest {

    @Test
    public void testGetAbsolutePathByUpperDirectory() {

        String currentPath = "\\java\\path\\test\\";
        String relativePath = "../../test01/test.js";

        Optional<String> absolutePathOptional = AbsolutePathUtils.getAbsolutePathByRelativePath(currentPath, relativePath);

        Assert.assertEquals("\\java\\test01\\test.js", absolutePathOptional.get());
    }

    @Test
    public void testGetAbsolutePathBySameDirectory() {

        String currentPath = "\\java\\path\\test\\";
        String relativePath = "./test01/test.js";

        Optional<String> absolutePathOptional = AbsolutePathUtils.getAbsolutePathByRelativePath(currentPath, relativePath);

        Assert.assertEquals("\\java\\path\\test\\test01\\test.js", absolutePathOptional.get());
    }

    @Test
    public void testFindSimilarPaths(){
        String sourcePath = "C:/Users/USER/Desktop/nodejs/absolute/absolute.js";
        List<String> targetPaths = Lists.newArrayList("opt\\workspace\\absolute\\absolute.js",
                "opt\\workspace\\absolute.js", "opt\\workspace\\absolute\\route\\absolute.js", "\\opt\\workspace\\absolute\\absolute.js");

        Optional<List<String>> nearSourcePathOptional = AbsolutePathUtils.findSimilarSourcePaths(sourcePath, targetPaths);
        Assert.assertEquals(Lists.newArrayList("opt\\workspace\\absolute\\absolute.js", "\\opt\\workspace\\absolute\\absolute.js"), nearSourcePathOptional.get());
    }


}
