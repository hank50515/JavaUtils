import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class streamUtils {
    /**
     * Filter same value by stream filter
     * e.g.
     * list.stream().filter( distinctByKey(p -> p.getId()) )
     *
     * param Function
     * return Predicate
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor){
        Map<Object, Boolean> map = Maps.newHashMap();

        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
