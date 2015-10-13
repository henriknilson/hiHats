package hihats.electricity.util;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("ParseDealHelper")
public class ParseDealHelper extends ParseObject {

    public ParseDealHelper() { super(); }

    public String getObjectId() {
        return getString("objectId");
    }

    public String getName() {
        return getString("name");
    }

    public String getAuthor() {
        return getString("author");
    }

    public int getPoints() {
        return getInt("points");
    }

    public String getDescription() {
        return getString("description");
    }
}
