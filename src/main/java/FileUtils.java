import org.apache.commons.lang3.StringUtils;

public class FileUtils {
    /**
     * 依據targetPath 的Separator將sourcePath取得為相同的Separator 取代字串中之分隔線會根據 target
     * 的分隔線來轉換若 target 字串包含"/"，source 則取代成"/" 若為"\"，則取代成"\",如果 target 同時有 "\","/"
     * 和沒有斜線的話就不處理
     * <p>
     * param String sourcePath, String targetPath
     *
     * @return String
     */
    public static String convertToSameSeparator(String sourcePath, String targetPath) {
        boolean isContainsSlash = StringUtils.contains(targetPath, "/");
        boolean isContainsBackSlash = StringUtils.contains(targetPath, "\\");

        if (isContainsSlash && isContainsBackSlash) {
            return sourcePath;
        } else if (isContainsBackSlash) {
            return sourcePath.replaceAll("\\/", "\\\\");
        } else if (isContainsSlash) {
            return sourcePath.replaceAll("\\\\", "\\/");
        } else {
            return sourcePath;
        }
    }
}
