package hihats.electricity.model;

import com.parse.*;

import java.util.Date;

import hihats.electricity.model.IDeal;

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

    public void setName(String name) {
        put("name", name);
    }

    public void setCreatedAt(Date date) {
        if(date == null) {
            date = new Date();
        }
        put("createdAt", date);
    }

    public void setUpdatedAt(Date date) {
        if(date == null) {
            date = new Date();
        }
        put("updatedAt", date);
    }

    public void setAuthor(String author) {
        if(author == null) {
            author = "";
        }
        put("author", author);
    }

    public void setDescription(String description) {
        if(description == null) {
            description = "";
        }
        put("description", description);
    }

    public void setPoints(int points) {
        put("points", points);
    }

    public void setImage(int image) {
        put("image", image);
    }

}
