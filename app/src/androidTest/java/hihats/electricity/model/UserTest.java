package hihats.electricity.model;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by Pertta on 15-10-08.
 */

public class UserTest extends TestCase {
    private User uone = null, utwo = null;
    public UserTest(String name) {
        super(name);
    }
    public void setUp() {
        uone = User.getInstance("Test1", "1234", 10);
        System.out.println("Created singleton user: " + uone);
        System.out.println(uone.getUserName());
        utwo = User.getInstance("Test2", "1337", 100);
        System.out.println(utwo.getUserName());
        System.out.println("Created singleton user: " + utwo);
        testUnique();

    }
    public void testUnique() {
        System.out.println("checking singletons for equality");
        Assert.assertEquals(true, uone == utwo);
    }
}
