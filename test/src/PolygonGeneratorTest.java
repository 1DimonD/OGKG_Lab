package src;

import Utils.PolygonGenerator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.awt.geom.Point2D;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static src.Point2DMatcher.hasRadiusGreaterThanOrEqualTo;

class PolygonGeneratorTest {

    @ParameterizedTest(name = "Check for validity")
    @CsvSource({
            "0, 0, 100, 50, 0.5, 0.3, 6",
            "10, 20, 50, 30, 0.2, 0.1, 8",
            "-5, 5, 80, 40, 0.8, 0.2, 5"
    })
    void generatePolygon_validParameters_success(double centerX, double centerY, double avgRadius, double minRadius,
                                                 double irregularity, double spikiness, int numVertices) {
        Point2D center = new Point2D.Double(centerX, centerY);

        List<Point2D> polygon = PolygonGenerator.generatePolygon(center, avgRadius, minRadius,
                                                                irregularity, spikiness, numVertices);

        Assertions.assertNotNull(polygon);
        assertThat(polygon, everyItem(hasRadiusGreaterThanOrEqualTo(minRadius)));
        Assertions.assertEquals(numVertices, polygon.size());
    }

    @ParameterizedTest(name = "Check for invalid parameters")
    @CsvSource({
            "-5, 0, 100, 50, -0.5, 0.3, 6",
            "10, 20, 50, 30, 0.2, -0.1, 8",
            "-5, 5, -80, 40, 0.8, 0.2, 5"
    })
    void generatePolygon_invalidParameters_throwException(double centerX, double centerY, double avgRadius,
                                                          double minRadius, double irregularity, double spikiness,
                                                          int numVertices) {
        Point2D center = new Point2D.Double(centerX, centerY);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            PolygonGenerator.generatePolygon(center, avgRadius, minRadius, irregularity, spikiness, numVertices);
        });
    }
}