import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AbsolutePathUtils {
    private static final String UPPER_DOT = "..";

    private static final String DOT = ".";

    private static final String UPPER_DIRECTORY_PATTERN = "(?:\\.\\.\\/)|(?:\\.\\.\\\\)";

    private static Logger log = Logger.getLogger(AbsolutePathUtils.class.getClass());

    /**
     * 根據相對路徑的規則和目前路徑的位子，算出相對路徑的絕對路徑
     * currentPath = /path/doc/
     * relativePath = ./project.js
     * 會得到 /path/doc/project.js
     * 斜線會根據 source 的方向
     * @param currentPath - 目前在的路徑不要帶 file Name 進來
     * @param relativePath - 相對路徑
     * @return 根據目前在的路徑和相對路徑算出來的絕對路徑，斜線方向是 current path 的方向
     */
    public static Optional<String> getAbsolutePathByRelativePath(String currentPath, String relativePath){
        if(StringUtils.isBlank(currentPath) || StringUtils.isBlank(relativePath)){
            log.debug("帶入的參數有空值，請檢查後再帶入 !");
            return Optional.empty();
        }

        // 先將 relative paht 轉成跟 current path 同邊的 slash
        relativePath = FileUtils.convertToSameSeparator(relativePath, currentPath);
        // 先切除路徑最後的斜線方便判斷 /java/path/ 會變成 /java/path
        currentPath = StringUtils.stripEnd(currentPath, File.separator);

        if(StringUtils.contains(relativePath, UPPER_DOT + File.separator)){
            return Optional.of(getAbsoluteByUpperDirectory(currentPath, relativePath));
        } else if(StringUtils.contains(relativePath, DOT + File.separator)){
            return Optional.of(getAbsoluteBySameDirectory(currentPath, relativePath));
        } else if(!StringUtils.startsWith(relativePath, File.separator)) {
            relativePath = DOT + File.separator + relativePath;
            return Optional.of(getAbsoluteBySameDirectory(currentPath, relativePath));
        }

        log.debug("找不到相對應的路徑，請檢查是否是相對路徑");
        return Optional.empty();
    }

    /**
     * 用遞迴來計算哪個路徑最接近 source path
     * source  = C://users/user/router/route/route.js
     * targets = opt/workspace/route.js, opt/workspace/router/route.js, opt/workspace/router/route/route.js
     * 會解出 /opt/workspace/router/route/route.js 是最接近的
     * 目前會將斜線內部處理掉，然後回傳符合的 targetPath，而不會將斜線轉成 source 的，如果需要可以再額外開 method
     * @param sourcePath
     * @param targetPaths - 希望是 entity location
     * @return 最相近的路徑 targetPath
     */
    public static Optional<List<String>> findSimilarSourcePaths(String sourcePath, Collection<String> targetPaths){
        if(StringUtils.isBlank(sourcePath) || CollectionUtils.isEmpty(targetPaths)){
            log.debug("帶入的參數有空值，請檢查後再帶入 !");
            return Optional.empty();
        }

        List<String> similarPaths = findSimilarPaths(sourcePath, targetPaths, 0);
        if(CollectionUtils.isEmpty(similarPaths)){
            return Optional.empty();
        }

        return Optional.of(similarPaths);
    }

    private static String getAbsoluteByUpperDirectory(String currentPath, String relativePath) {
        Pattern upperDirectoryPattern = Pattern.compile(UPPER_DIRECTORY_PATTERN);
        Matcher upperDirectoryMatcher = upperDirectoryPattern.matcher(relativePath);
        Integer upperDirectoryCount = 0;
        while (upperDirectoryMatcher.find()) {
            upperDirectoryCount++;
        }

        // 將路徑根據有幾個上一層去扣除
        // /java/path/java 如果有2層就會變成 /java/path
        for (int i = 0; i < upperDirectoryCount; i++) {
            currentPath = StringUtils.substringBeforeLast(currentPath, File.separator);
        }

        String relativePathWithoutUpperDirectory = relativePath.replaceAll(UPPER_DIRECTORY_PATTERN, "");

        return currentPath + File.separator + relativePathWithoutUpperDirectory;
    }

    private static String getAbsoluteBySameDirectory(String currentPath, String relativePath) {
        String relativePathWithoutSameDirectory = StringUtils.substringAfterLast(relativePath, DOT + File.separator);

        return currentPath + File.separator + relativePathWithoutSameDirectory;
    }

    /**
     * 用遞迴來計算哪個路徑最接近 source path
     * source  = C://users/user/router/route/route.js
     * targets = opt/workspace/route.js, opt/workspace/router/route.js, opt/workspace/router/route/route.js
     * 會解出 /opt/workspace/router/route/route.js 是最接近的
     * 會使用切割斜線方式來判斷，由後往前切，跑越多次切的斜線越少，直到找到最接近的
     * @param sourcePath
     * @param targetPaths
     * @param count 用來計次跑多少次，跑越多次切掉的斜線越少
     * @return 最近近的路徑
     */
    private static List<String> findSimilarPaths(String sourcePath, Collection<String> targetPaths, Integer count){
        List<String> nearTargetPaths = targetPaths.stream().filter(targetPath -> {
            // 找出有多少斜線
            int countMatches = StringUtils.countMatches(targetPath, File.separator);
            // 跑越多次切的斜線越少，所以要減去 count，因為是從0開始所以要多 +1
            String[] targetPathOfEnd = StringUtils.splitByWholeSeparatorPreserveAllTokens(targetPath, File.separator, countMatches - count + 1);
            // 轉成同一邊斜線
            String sameSlashsourcePath = FileUtils.convertToSameSeparator(sourcePath, targetPath);
            // 這邊要多減一的原因是從0 開始計算
            return StringUtils.endsWithIgnoreCase(sameSlashsourcePath, targetPathOfEnd[targetPathOfEnd.length - 1]);
        }).collect(Collectors.toList());

        // 如果有找到就找下去
        if(!nearTargetPaths.isEmpty()){
            // 將跑的次數 +1
            return findSimilarPaths(sourcePath, nearTargetPaths, count + 1);
        }

        // 找不到就傳最後找到的結果
        return Lists.newArrayList(targetPaths);
    }
}
