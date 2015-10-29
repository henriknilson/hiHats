package hihats.electricity.model;

import java.util.Date;

/**
 * Created by fredrikkindstrom on 28/10/15.
 */
public interface IRide {

    Date getDate();
    String getFrom();
    String getTo();
    int getPoints();
    double getDistance();
    String getUser();
}
