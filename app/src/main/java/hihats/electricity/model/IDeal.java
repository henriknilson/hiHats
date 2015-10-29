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
}
