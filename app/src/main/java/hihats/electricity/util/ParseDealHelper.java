package hihats.electricity.util;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("ParseDealHelper")
public class ParseDealHelper extends ParseObject {

    public ParseDealHelper() {
    }

    public String getObjectId() {
        return getString("objectId");
    }

    public String getName() {
        return getString("Name");
    }

    public String getAuthor() {
        return getString("Author");
    }

    public int getPoints() {
        return getInt("Points");
    }

    public String getDescription() {
        return getString("Description");
    }
}
