package hihats.electricity.net;

import junit.framework.TestCase;

/**
 * Created by fredrikkindstrom on 01/10/15.
 */
public class HttpHandlerTest extends TestCase {

    HttpHandler tester;

    protected void setUp() throws Exception {
        super.setUp();
        tester = new HttpHandler();
    }

    public void testGetResponseNoAccess() throws Exception {
        boolean thrown = false;
        try {
            tester.getJSONResponse("bad url");
        } catch (AccessErrorException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
}