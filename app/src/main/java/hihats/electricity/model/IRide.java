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
    int getDistance();
    String getUser();

    void setDate(Date date);
    void setFrom(String from);
    void setTo(String to);
    void setPoints(int points);
    void setDistance(int distance);
    void setUser(String user);

}
