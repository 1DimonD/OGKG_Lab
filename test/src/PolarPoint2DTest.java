package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import visibilityPolygon.PolarPoint2D;

import java.awt.geom.Point2D;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static visibilityPolygon.CommonUtils.Eps;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PolarPoint2DTest {

    private Point2D cartesianPoint;
    private PolarPoint2D polarPoint;

    @BeforeAll
    void setUp() {
        cartesianPoint = new Point2D.Double(1, 1);
    }

    @BeforeEach
    void initialize() {
        polarPoint = PolarPoint2D.cartesianToPolar(cartesianPoint);
    }

    @Test
    void testCartesianToPolar() {
        assertThat(polarPoint.getR(), closeTo(Math.sqrt(2), Eps));
        assertThat(polarPoint.getTheta(), closeTo(Math.PI / 4, Eps));
    }

    @Test
    void testRotateClockWise() {
        double originalTheta = polarPoint.getTheta();

        org.junit.jupiter.api.Assumptions.assumeTrue(!polarPoint.isOrigin());

        polarPoint.rotateClockWise(Math.PI / 4);

        assertThat(polarPoint.getTheta(), closeTo(originalTheta - Math.PI / 4, Eps));
    }

    @Test
    void testIsOrigin() {
        Point2D origin = new Point2D.Double(0, 0);
        PolarPoint2D polarOrigin = PolarPoint2D.cartesianToPolar(origin);

        Assertions.assertTrue(polarOrigin.isOrigin());
    }
}


