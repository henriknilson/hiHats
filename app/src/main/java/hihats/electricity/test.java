package hihats.electricity;

import hihats.electricity.api.AccessErrorException;
import hihats.electricity.api.ApiDataHelper;

/**
 * Created by fredrikkindstrom on 23/09/15.
 */
public class test {

    public static void main(String[] args) {
        try {
            ApiDataHelper.getMostRecentLocationForBus("Ericsson$100021");
        } catch (AccessErrorException e) {
            e.printStackTrace();
        }
    }
}
