package src;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import visibilityPolygon.CCWPolygon;
import visibilityPolygon.LineSegment;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CCWPolygonTest {

    @Test
    @DisplayName("Get vertices")
    void getVertices_shouldReturnCorrectVertices() {
        List<Point2D> vertices = Arrays.asList(
                new Point2D.Double(0, 0),
                new Point2D.Double(1, 1),
                new Point2D.Double(2, 2)
        );
        CCWPolygon polygon = new CCWPolygon(vertices);

        List<Point2D> result = polygon.getVertices();

        assertThat(result, contains(
                Matchers.equalTo(new Point2D.Double(0, 0)),
                Matchers.equalTo(new Point2D.Double(1, 1)),
                Matchers.equalTo(new Point2D.Double(2, 2))
        ));
    }

    @Test
    @DisplayName("Add vertex")
    void addVertex_shouldAddVertexToList() {
        List<Point2D> vertices = Arrays.asList(
                new Point2D.Double(0, 0),
                new Point2D.Double(1, 1)
        );
        CCWPolygon polygon = new CCWPolygon(vertices);

        Point2D newVertex = new Point2D.Double(2, 2);

        polygon.addVertex(newVertex);
        List<Point2D> result = polygon.getVertices();

        assertThat(result, hasSize(3));
        assertThat(result, contains(
                Matchers.equalTo(new Point2D.Double(0, 0)),
                Matchers.equalTo(new Point2D.Double(1, 1)),
                Matchers.equalTo(new Point2D.Double(2, 2))
        ));
    }

    @Test
    @DisplayName("Get LineSegments")
    void getEdges_shouldReturnCorrectEdges() {
        List<Point2D> vertices = Arrays.asList(
                new Point2D.Double(0, 0),
                new Point2D.Double(1, 1),
                new Point2D.Double(2, 2)
        );
        CCWPolygon polygon = new CCWPolygon(vertices);

        List<LineSegment> result = polygon.getEdges();

        assertThat(result, contains(
                Matchers.equalTo(new LineSegment(new Point2D.Double(0, 0), new Point2D.Double(1, 1))),
                Matchers.equalTo(new LineSegment(new Point2D.Double(1, 1), new Point2D.Double(2, 2))),
                Matchers.equalTo(new LineSegment(new Point2D.Double(2, 2), new Point2D.Double(0, 0)))
        ));
    }

}
