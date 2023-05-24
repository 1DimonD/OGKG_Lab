package src;

import org.junit.jupiter.api.Test;
import visibilityPolygon.LineSegment;

import java.awt.geom.Point2D;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static visibilityPolygon.CommonUtils.Eps;
import static visibilityPolygon.LineSegment.pointOnSegment;
import static visibilityPolygon.LineSegment.intersectSegments;

class LineSegmentTest {

    @Test
    void testPointOnSegment() {
        Point2D p = new Point2D.Double(1, 1);
        LineSegment segment = new LineSegment(new Point2D.Double(0, 0), new Point2D.Double(2, 2));

        assertThat(pointOnSegment(p, segment), is(true));
    }

    @Test
    void testIntersectProper() {
        LineSegment segment1 = new LineSegment(new Point2D.Double(0, 0), new Point2D.Double(2, 2));
        LineSegment segment2 = new LineSegment(new Point2D.Double(1, 0), new Point2D.Double(1, 2));

        assertThat(segment1.intersectProper(segment2), is(true));
    }

    @Test
    void testIntersectSegments() {
        LineSegment segment1 = new LineSegment(new Point2D.Double(0, 0), new Point2D.Double(2, 2));
        LineSegment segment2 = new LineSegment(new Point2D.Double(1, 0), new Point2D.Double(1, 2));

        Point2D intersection = intersectSegments(segment1, segment2);

        assertThat(intersection, is(notNullValue()));
        assertThat(intersection.getX(), closeTo(1.0, Eps));
        assertThat(intersection.getY(), closeTo(1.0, Eps));
    }
}
