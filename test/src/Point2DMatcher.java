package src;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.awt.geom.Point2D;

public class Point2DMatcher extends TypeSafeMatcher<Point2D> {
    private final double minRadius;

    public Point2DMatcher(double minRadius) {
        this.minRadius = minRadius;
    }

    @Override
    protected boolean matchesSafely(Point2D point2D) {
        double radius = Math.pow(point2D.getX(), 2) + Math.pow(point2D.getY(), 2);
        return radius >= minRadius;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("point with radius greater than or equal to ").appendValue(minRadius);
    }

    public static Matcher<Point2D> hasRadiusGreaterThanOrEqualTo(double minRadius) {
        return new Point2DMatcher(minRadius);
    }
}
