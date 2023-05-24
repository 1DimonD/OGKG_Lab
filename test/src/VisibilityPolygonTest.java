package src;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import visibilityPolygon.CCWPolygon;
import visibilityPolygon.VisibilityPolygon;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class VisibilityPolygonTest {

    @ParameterizedTest
    @MethodSource("computeVisPolArguments")
    public void testComputeVisPol(CCWPolygon inputPol, List<Point2D> viewPoints, List<CCWPolygon> expected) {
        List<CCWPolygon> result = VisibilityPolygon.computeVisPol(inputPol, viewPoints);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> computeVisPolArguments() {
        CCWPolygon polygon1 = new CCWPolygon(Arrays.asList(
                new Point2D.Double(0, 0),
                new Point2D.Double(0, 5),
                new Point2D.Double(5, 5),
                new Point2D.Double(5, 0)
        ));

        CCWPolygon polygon2 = new CCWPolygon(Arrays.asList(
                new Point2D.Double(0, 0),
                new Point2D.Double(0, 5),
                new Point2D.Double(5, 5),
                new Point2D.Double(5, 0),
                new Point2D.Double(2, 2)
        ));

        List<Point2D> viewPoints1 = Arrays.asList(
                new Point2D.Double(2.5, 2.5),
                new Point2D.Double(1, 1)
        );
        List<CCWPolygon> expected1 = Arrays.asList(
                new CCWPolygon(Arrays.asList(
                        new Point2D.Double(2.5, 2.5),
                        new Point2D.Double(0, 5),
                        new Point2D.Double(5, 5),
                        new Point2D.Double(5, 0),
                        new Point2D.Double(0, 0)
                )),
                new CCWPolygon(Arrays.asList(
                        new Point2D.Double(1, 1),
                        new Point2D.Double(0, 5),
                        new Point2D.Double(5, 5),
                        new Point2D.Double(5, 0),
                        new Point2D.Double(0, 0)
                ))
        );

        List<Point2D> viewPoints2 = Arrays.asList(
                new Point2D.Double(2, 2),
                new Point2D.Double(3, 3)
        );
        List<CCWPolygon> expected2 = Arrays.asList(
                new CCWPolygon(Arrays.asList(
                        new Point2D.Double(2, 2),
                        new Point2D.Double(2.5, 2.5),
                        new Point2D.Double(5, 5),
                        new Point2D.Double(5, 0),
                        new Point2D.Double(0, 0)
                )),
                new CCWPolygon(Arrays.asList(
                        new Point2D.Double(3, 3),
                        new Point2D.Double(2.5, 2.5),
                        new Point2D.Double(5, 5),
                        new Point2D.Double(5, 0),
                        new Point2D.Double(0, 0)
                ))
        );

        return Stream.of(
                Arguments.of(polygon1, viewPoints1, expected1),
                Arguments.of(polygon2, viewPoints2, expected2)
        );
    }
}


