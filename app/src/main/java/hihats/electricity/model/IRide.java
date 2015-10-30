package hihats.electricity.model;

import java.util.Date;

/**
 * This interface represents how a Ride should act and what it should be capable of doing.
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
