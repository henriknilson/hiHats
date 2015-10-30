package hihats.electricity.model;

/**
 * This interface represents how a Bus should act and what it should be capable of doing.
 */

public interface IBus {

    String getDgw();
    String getVin();
    String getRegNr();
    String getSystemId();
    BusType getBusType();
    DatedPosition getDatedPosition();
    float getSpeed();
    float getBearing();

    void setDatedPosition(DatedPosition datedPosition);
    void setSpeed(float speed);
    void setBearing(float bearing);
}
