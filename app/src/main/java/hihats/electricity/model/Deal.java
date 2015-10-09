package hihats.electricity.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by axel on 2015-09-24.
 */
public class Deal {

    private int objectId;
    private String name;
    private String author;
    private String description;
    private int points;

    public Deal(int objectId, String name, String author, String description, int points) {
        this.objectId = objectId;
        this.name = name;
        this.author = author;
        this.description = description;
        this.points = points;
    }

    public int getobjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public int getPoints() {
        return points;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
