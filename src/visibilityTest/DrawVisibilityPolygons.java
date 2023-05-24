package visibilityTest;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Utils.PolygonGenerator;
import visibilityPolygon.CCWPolygon;
import visibilityPolygon.VisibilityPolygon;

import static visibilityTest.DrawVisibilityPolygons.*;

public class DrawVisibilityPolygons {

	public static final int CURRENT_WIDTH = 800, CURRENT_HEIGHT = 600, OFFSET = 100;
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		List<Point2D> vertices = null;

		while(true) {
			System.out.print("Choose the way to draw a polygon:\n" +
					"1 - user input\n" +
					"2 - random points (auto)\n" +
					"3 - random points (user settings)\n" +
					"any int - exit\n" +
					"Your choice: ");

			int choice = sc.nextInt();


			switch (choice) {
				case 1: {
					vertices = GetUserInput();

					if (!MainUtils.isSimple(vertices)) {
						System.out.println("Polygon is not simple. Program aborted.");
						return;
					}

					break;
				}
				case 2: {
					vertices = PolygonGenerator.generatePolygon(new Point2D.Double(0, 0),
							10, 5,
							0.3, 0.6,
							26);
					break;
				}
				case 3: {
					vertices = getCustomGeneratedPolygon();
					break;
				}
				default:
					System.out.println("Exit program");
					System.exit(0);
			}

			System.out.println(vertices);
			CCWPolygon inputPol = new CCWPolygon(vertices);

			String s = "";
			do {
				List<Point2D> viewPoints = new ArrayList<>();
				double x, y;
				System.out.print("Enter view point (example: 15 20.4): ");
				x = sc.nextDouble();
				y = sc.nextDouble();
				viewPoints.add(new Point2D.Double(x, y));

				Figure fig = new Figure(inputPol, viewPoints, MainUtils.getScale(vertices));

				System.out.println("Do you want to enter other viewpoint? (Y/n)");
				sc.nextLine();
				s = sc.nextLine();
			} while(s.charAt(0) == 'Y' || s.charAt(0) == 'y');
		}
	}

//	private static List<Point2D> getConstPolygon() {
//		List<Point2D> vertices = new ArrayList<>();
//		vertices.add(new Point2D.Double(0, 6));
//		vertices.add(new Point2D.Double(2, 8));
//		vertices.add(new Point2D.Double(2, 10));
//		vertices.add(new Point2D.Double(0, 10));
//		vertices.add(new Point2D.Double(0, -2));
//		vertices.add(new Point2D.Double(4, -4));
//		vertices.add(new Point2D.Double(-4, -4));
//		vertices.add(new Point2D.Double(-6, 0));
//		vertices.add(new Point2D.Double(2, -2));
//		vertices.add(new Point2D.Double(-4, 6));
//		return MainUtils.makeCCWOrder(vertices);
//	}

	private static List<Point2D> GetUserInput() {
		List<Point2D> res = new ArrayList<>();

		int numVertices;
		System.out.print("Enter number of vertices to be provided: ");
		numVertices = sc.nextInt();

		double x, y;
		for(int i = 0; i < numVertices; i++) {
			System.out.print("Enter coordinates of N" + (i+1) + " point (example: 15 20.4): ");
			x = sc.nextDouble(); y = sc.nextDouble();
			res.add(new Point2D.Double(x, y));
		}

		return MainUtils.makeCCWOrder(res);
	}

	private static List<Point2D> getCustomGeneratedPolygon() {
		double avgRadius, minRadius, irregularity, spikiness;
		int numVertices;

		System.out.print("Enter number of vertices to be generated: ");
		numVertices = sc.nextInt();
		System.out.print("Enter average radius for vertices according to point O(0,0): ");
		avgRadius = sc.nextDouble();
		System.out.print("Enter minimum radius for vertices: ");
		minRadius = sc.nextDouble();
		System.out.print("Enter coefficient of irregularity (vertices generates CCW according to center point " +
				"with equal offsets, irregularity influences offsets value): ");
		irregularity = sc.nextDouble();
		System.out.print("Enter spikiness (influences range of radius from average): ");
		spikiness = sc.nextDouble();

		return PolygonGenerator.generatePolygon(new Point2D.Double(0, 0),
				avgRadius, minRadius,
				irregularity, spikiness,
				numVertices);
	}

}

class MainUtils {
	public static boolean isSimple(List<Point2D> V) {
		if (V == null) {
			return false;
		}

		int len = V.size();

		if (len < 4) {
			return true;
		}

		for (int i = 0; i < len - 1; i++) {
			for (int j = i + 2; j < len; j++) {

				if ((i == 0) && (j == (len - 1))) {
					continue;
				}

				boolean cut = Line2D.linesIntersect(
						V.get(i).getX(),
						V.get(i).getY(),
						V.get(i + 1).getX(),
						V.get(i + 1).getY(),
						V.get(j).getX(),
						V.get(j).getY(),
						V.get((j + 1) % len).getX(),
						V.get((j + 1) % len).getY());

				if (cut) {
					return false;
				}
			}
		}

		return true;
	}

	public static Point2D findCentroid(List<Point2D> points) {
		int x = 0;
		int y = 0;

		for (Point2D p : points) {
			x += p.getX();
			y += p.getY();
		}

		return new Point2D.Double((double) x / points.size(), (double) y / points.size());
	}

	public static List<Point2D> makeCCWOrder(List<Point2D> vertices) {
		Point2D center = findCentroid(vertices);
		Collections.sort(vertices, (a, b) -> {
			double a1 = (Math.toDegrees(Math.atan2(a.getY() - center.getY(), a.getX() - center.getX())) + 360) % 360;
			double a2 = (Math.toDegrees(Math.atan2(b.getY() - center.getY(), b.getX() - center.getX())) + 360) % 360;
			return (int) (a1 - a2);
		});
		return vertices;
	}

	public static double getScale(List<Point2D> vertices) {
		double maxX = vertices.get(0).getX(), maxY = vertices.get(0).getY(),
				minX = vertices.get(0).getX(), minY = vertices.get(0).getY();
		for(int i = 1; i < vertices.size(); i++) {
			maxX = Math.max(maxX, vertices.get(i).getX());
			minX = Math.min(minX, vertices.get(i).getX());
			maxY = Math.max(maxY, vertices.get(i).getY());
			minY = Math.min(minY, vertices.get(i).getY());
		}

		double widthScale = Math.min(Math.abs((CURRENT_WIDTH-OFFSET)/maxX/2), Math.abs((CURRENT_WIDTH-OFFSET)/minX/2)),
				heightScale = Math.min(Math.abs((CURRENT_HEIGHT-OFFSET)/maxY/2), Math.abs((CURRENT_HEIGHT-OFFSET)/minY/2));
		return Math.floor(Math.min(widthScale, heightScale));
	}
}


class Figure extends JPanel {

	private Path2D inputPol;
	private List<Path2D> visPolygons;
	private List<Path2D> viewPoints;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g.create();
		this.setBackground(Color.GRAY);

		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;

		g2d.translate(centerX, centerY);
		g2d.scale(1, -1);

		g2d.setColor(Color.BLACK);
		g2d.fill(inputPol);

		g2d.setColor(Color.WHITE);
		for (Path2D curr : visPolygons)
			g2d.fill(curr);

		g2d.setColor(Color.RED);
		for (Path2D curr : viewPoints) {
			g2d.fill(curr);
		}

		g2d.dispose();
	}

	public Figure(CCWPolygon inputPol, List<Point2D> viewPoints, double scaling) {
		List<CCWPolygon> CCWVisPolygons = VisibilityPolygon.computeVisPol(inputPol, viewPoints);

		visPolygons = CCWVisPolygons.stream().map(x -> x.scale(scaling).getPolygon()).collect(Collectors.toList());

		this.inputPol = inputPol.scale(scaling).getPolygon();

		this.viewPoints = new ArrayList<>();
		for (Point2D curr : viewPoints) {
			Path2D.Double temp = new Path2D.Double();
			temp.append(new Ellipse2D.Double(curr.getX() * scaling, curr.getY() * scaling, 5, 5), true);
			this.viewPoints.add(temp);
		}

		JFrame frame = new JFrame();
		frame.setTitle("Visibility");
		frame.setSize(CURRENT_WIDTH, CURRENT_HEIGHT);

//		frame.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				System.exit(0);
//			}
//		});

		Container contentPane = frame.getContentPane();
		contentPane.add(this);
		frame.setVisible(true);
	}
}
