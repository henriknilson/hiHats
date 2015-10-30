package hihats.electricity.util;

/**
 * This class is called upon when GreenPoints need to be calculated.
 * By having a separate class you can always go in and calibrate how the points are calculated.
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

    /**
     * Call this when you want to compute points in real time
     * based on traveled distance and passed time.
     * @param distance The distance traveled to compute points for.
     * @param time The time passed to compute points for.
     * @return Computed points.
     */
    public double getLivePoints(int distance, long time) {
        double pointsFromDistance = distance * 0.01;
        double pointsFromTime = time * (0.001 * 0.01);
        return pointsFromDistance + pointsFromTime;
    }

    /**
     * Called to round up the double added live
     * points to a finished points integer.
     * @param livePoints The summed up live points.
     * @return Rounded off points.
     */
    public int roundOffPoints(double livePoints) {
        return  (int) Math.round(livePoints);
    }
}
