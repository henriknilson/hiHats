package hihats.electricity.model;

import java.util.Date;

/**
 * Created by fredrikkindstrom on 28/10/15.
 */
public interface IDeal {

    String getName();
    Date getCreatedAt();
    Date getUpdatedAt();
    String getAuthor();
    String getDescription();
    int getPoints();
    int getImage();

    void setName(String name);
    void setCreatedAt(Date date);
    void setUpdatedAt(Date date);
    void setAuthor(String author);
    void setDescription(String description);
    void setPoints(int points);
    void setImage(int image);


}
