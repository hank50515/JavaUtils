import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class StringUtils2 extends StringUtils {

    /**
     * text is contains collection keywords ignoreCase
     *
     * <pre>
     * containsIgnoreCaseWithKeywords('abcde', 'f','g','h','i','b','k') = true;
     * containsIgnoreCaseWithKeywords('aBcde', 'f','g','h','i','b','k') = true;
     * containsIgnoreCaseWithKeywords('abcde', 'f','g','h','i','k')     = false;
     * containsIgnoreCaseWithKeywords('abcde', 'd','e')		  = true;
     * containsIgnoreCaseWithKeywords('f', 1,2,3,4,5)                   = false;
     * <pre>
     *
     * @param text
     * @param keywords
     * @return is contains
     */
    public static boolean containsAnyIgnoreCase(String text, Collection<String> keywords){
        return keywords.stream().parallel().anyMatch(keyword -> StringUtils.containsIgnoreCase(text, keyword));
    }

    /**
     * text is contains collection keywords
     *
     * <pre>
     * containsIgnoreCaseWithKeywords('abcde', 'f','g','h','i','b','k') = true;
     * containsIgnoreCaseWithKeywords('abcde', 'f','g','h','i','k')     = false;
     * containsIgnoreCaseWithKeywords('abcde', 'd','e')		  = true;
     * containsIgnoreCaseWithKeywords('f', 1,2,3,4,5)                   = false;
     * <pre>
     *
     * @param text
     * @param keywords
     * @return is contains
     */
    public static boolean containsAny(String text, Collection<String> keywords){
        return keywords.stream().parallel().anyMatch(keyword -> StringUtils.contains(text, keyword));
    }
}
