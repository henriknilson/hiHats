package hihats.electricity.model;

/**
 * This class represents a local version of the user.
 */

public class CurrentUser {

    private static CurrentUser instance;

    private String userName;
    private String userId;
    private int points;


    private CurrentUser() {}

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }


    /*
    Getters
     */

    public String getUserName() {
        return userName;
    }

    public int getPoints() {
        return points;
    }

    /*
    Setters
     */

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setRides() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrentUser that = (CurrentUser) o;

        if (points != that.points) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null)
            return false;
        return !(userId != null ? !userId.equals(that.userId) : that.userId != null);

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + points;
        return result;
    }
}
