package hihats.electricity.util;

/**
 * Created by fredrikkindstrom on 29/10/15.
 */
public class GreenPointsCalculator {

    private static GreenPointsCalculator instance;

    private GreenPointsCalculator(){}

    public static GreenPointsCalculator getInstance() {
        if (instance == null) {
            instance = new GreenPointsCalculator();
        }
        return instance;
    }

    public double getLivePoints(int distance, long time) {
        double pointsFromDistance = distance * 0.01;
        double pointsFromTime = time * (0.001 * 0.01);
        return pointsFromDistance + pointsFromTime;
    }

    public int getPoints(double livePoints) {
        return  (int) Math.round(livePoints);
    }
}
