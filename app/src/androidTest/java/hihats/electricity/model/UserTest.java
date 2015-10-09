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
        uone = User.getInstance();
        System.out.println("Created singleton user: " + uone);
        System.out.println(uone.getUserName() + uone.getPoints());
        utwo = User.getInstance();
        System.out.println("Created singleton user: " + utwo);
        System.out.println(utwo.getUserName() + utwo.getPoints());
        testUnique();

    }
    public void testUnique() {
        System.out.println("checking singletons for equality");
        Assert.assertEquals(true, uone == utwo);
    }
}
