import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class StreamUtilsUnitTest {

    @Test
    public void distinct() {

        User user = new User(1, "ray");
        User user2 = new User(2, "ray2");
        User user3 = new User(1, "ray3");

        List<User> users = Lists.newArrayList(user, user2, user3);

        users = users.stream()
                .filter(StreamUtils.distinctByKey(User::getId))
                .collect(Collectors.toList());

        Assert.assertEquals(Lists.newArrayList(user, user2), users);
    }

    public class User {
        Integer id;

        String name;

        public User(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return this.id;
        }
    }
}
