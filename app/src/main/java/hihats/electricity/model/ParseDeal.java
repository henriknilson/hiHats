package hihats.electricity.model;

import com.parse.*;

import java.util.Date;

/**
 * Created by axel on 2015-09-24.
 */
@ParseClassName("Deal")
public class ParseDeal extends ParseObject implements IDeal {

    public ParseDeal() {
        // Default constructor required
    }

    public String getName() {
        return getString("name");
    }

    public Date getCreatedAt() {
        return getDate("createdAt");
    }

    public Date getUpdatedAt() {
        return getDate("updatedAt");
    }

    public String getAuthor() {
        return getString("author");
    }

    public String getDescription() {
        return getString("description");
    }

    public int getPoints() {
        return getInt("points");
    }

    public int getImage() {
        return Integer.parseInt(getString("image"));
    }
}
