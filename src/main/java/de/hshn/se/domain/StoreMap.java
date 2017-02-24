package de.hshn.se.domain;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.parser.AWTPathProducer;
import org.apache.batik.util.XMLResourceDescriptor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A StoreMap.
 */
@Entity
@Table(name = "store_map")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StoreMap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "validity_start", nullable = false)
    private ZonedDateTime validityStart;

    @NotNull
    @Column(name = "validity_end", nullable = false)
    private ZonedDateTime validityEnd;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Lob
    @Column(name = "wall_map", nullable = false)
    private String wallMap;

    @NotNull
    @Lob
    @Column(name = "path_map", nullable = false)
    private String pathMap;

    @NotNull
    @Column(name = "dimension_x", nullable = false)
    private Double dimensionX;

    @NotNull
    @Column(name = "dimension_y", nullable = false)
    private Double dimensionY;

    @NotNull
    @Column(name = "scale", nullable = false)
    private Double scale;

    @ManyToOne
    @NotNull
    private Store store;

	@JsonIgnore
	@Transient
	private Set<Shape> walls = new HashSet<Shape>();

	@JsonIgnore
	@Transient
	private Set<Shape> paths = new HashSet<Shape>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getValidityStart() {
        return validityStart;
    }

    public StoreMap validityStart(ZonedDateTime validityStart) {
        this.validityStart = validityStart;
        return this;
    }

    public void setValidityStart(ZonedDateTime validityStart) {
        this.validityStart = validityStart;
    }

    public ZonedDateTime getValidityEnd() {
        return validityEnd;
    }

    public StoreMap validityEnd(ZonedDateTime validityEnd) {
        this.validityEnd = validityEnd;
        return this;
    }

    public void setValidityEnd(ZonedDateTime validityEnd) {
        this.validityEnd = validityEnd;
    }

    public String getUrl() {
        return url;
    }

    public StoreMap url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWallMap() {
        return wallMap;
    }

    public StoreMap wallMap(String wallMap) {
		this.walls.clear();
		if (wallMap != null && scale != null) {
			this.walls.addAll(resolveShapes(wallMap));

		}
        this.wallMap = wallMap;
        return this;
    }

    public void setWallMap(String wallMap) {
		this.walls.clear();
		if (wallMap != null && scale != null) {
			this.walls.addAll(resolveShapes(wallMap));

		}
        this.wallMap = wallMap;
    }

    public String getPathMap() {
        return pathMap;
    }

    public StoreMap pathMap(String pathMap) {
		this.paths.clear();
		if (pathMap != null && scale != null) {
			this.paths.addAll(resolveLines(pathMap));

		}
        this.pathMap = pathMap;
        return this;
    }

    public void setPathMap(String pathMap) {
		this.paths.clear();
		if (pathMap != null && scale != null) {
			this.paths.addAll(resolveLines(pathMap));

		}
        this.pathMap = pathMap;
    }

    public Double getDimensionX() {
        return dimensionX;
    }

    public StoreMap dimensionX(Double dimensionX) {
        this.dimensionX = dimensionX;
        return this;
    }

    public void setDimensionX(Double dimensionX) {
        this.dimensionX = dimensionX;
    }

    public Double getDimensionY() {
        return dimensionY;
    }

    public StoreMap dimensionY(Double dimensionY) {
        this.dimensionY = dimensionY;
        return this;
    }

    public void setDimensionY(Double dimensionY) {
        this.dimensionY = dimensionY;
    }

    public Double getScale() {
        return scale;
    }

    public StoreMap scale(Double scale) {
        this.scale = scale;
		if (wallMap != null && scale != null) {
			this.walls.addAll(resolveShapes(wallMap));
		}
		if (pathMap != null && scale != null) {
			this.paths.addAll(resolveLines(pathMap));

		}
        return this;
    }

    public void setScale(Double scale) {
        this.scale = scale;
		if (wallMap != null && scale != null) {
			this.walls.addAll(resolveShapes(wallMap));
		}
		if (pathMap != null && scale != null) {
			this.paths.addAll(resolveLines(pathMap));

		}
    }

    public Store getStore() {
        return store;
    }

    public StoreMap store(Store store) {
        this.store = store;
        return this;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StoreMap storeMap = (StoreMap) o;
        if (storeMap.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, storeMap.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StoreMap{" +
            "id=" + id +
            ", validityStart='" + validityStart + "'" +
            ", validityEnd='" + validityEnd + "'" +
            ", url='" + url + "'" +
            ", wallMap='" + wallMap + "'" +
            ", pathMap='" + pathMap + "'" +
            ", dimensionX='" + dimensionX + "'" +
            ", dimensionY='" + dimensionY + "'" +
            ", scale='" + scale + "'" +
            '}';
    }

	private Document getDocument(InputStream inputStream) throws IOException {
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		Document doc = f.createDocument("http://www.w3.org/2000/svg", inputStream);
		return doc;
	}

	public boolean isValidPath(double x, double y, double x2, double y2) {
		Line2D path = new Line2D.Double(x, y, x2, y2);
		// System.out.println("size of walls " + walls.size());
		// System.out.println("Wallmap: " + wallMap);
		for (Shape wall : walls) {
			ArrayList<double[]> areaPoints = new ArrayList<double[]>();
			ArrayList<Line2D.Double> areaSegments = new ArrayList<Line2D.Double>();
			double[] coords = new double[6];

			for (PathIterator pi = wall.getPathIterator(null); !pi.isDone(); pi.next()) {
				// The type will be SEG_LINETO, SEG_MOVETO, or SEG_CLOSE
				// Because the Area is composed of straight lines
				int type = pi.currentSegment(coords);
				// We record a double array of {segment type, x coord, y coord}
				double[] pathIteratorCoords = { type, coords[0], coords[1] };
				areaPoints.add(pathIteratorCoords);
			}

			double[] start = new double[3]; // To record where each polygon
											// starts

			for (int i = 0; i < areaPoints.size(); i++) {
				// If we're not on the last point, return a line from this point
				// to the next
				double[] currentElement = areaPoints.get(i);

				// We need a default value in case we've reached the end of the
				// ArrayList
				double[] nextElement = { -1, -1, -1 };
				if (i < areaPoints.size() - 1) {
					nextElement = areaPoints.get(i + 1);
				}

				// Make the lines
				if (currentElement[0] == PathIterator.SEG_MOVETO) {
					start = currentElement; // Record where the polygon started
											// to close it later
				}

				if (nextElement[0] == PathIterator.SEG_LINETO) {
					areaSegments.add(
							new Line2D.Double(currentElement[1], currentElement[2], nextElement[1], nextElement[2]));
				} else if (nextElement[0] == PathIterator.SEG_CLOSE) {
					areaSegments.add(new Line2D.Double(currentElement[1], currentElement[2], start[1], start[2]));
				}
			}
			
			for(Line2D li : areaSegments){
				if (li.intersectsLine(path)) {
					// System.out.println("intersetction: ");
					return false;
				}
			}


		}
		return true;
	}

	public double distanceToPath(double x, double y, double x2, double y2) {

		Line2D newPath = new Line2D.Double(x, y, x2, y2);

		double closestDist = 10;

		for (Shape path : paths) {
			Line2D line1 = (Line2D) path;
			double angle = angleBetween2Lines(line1, newPath);
			double anglePath = (Math
					.toDegrees(Math.atan2(line1.getY1() - line1.getY2(), line1.getX1() - line1.getX2()) + 360) % 360);

			double px1 = line1.getX1();
			double px2 = line1.getX2();
			double py1 = line1.getY1();
			double py2 = line1.getY2();

			if (angle < 20) {
				if ((anglePath < 45 || anglePath > 315) || (anglePath > 135 && anglePath < 225)) {
					if ((newPath.getX2() > px1 && newPath.getX2() < px2)
							|| (newPath.getX2() < px1 && newPath.getX2() > px2)) {
						double dist = line1.ptLineDist(newPath.getX2(), newPath.getY2());
						closestDist = dist < closestDist ? dist : closestDist;
					}
				} else {
					if ((newPath.getY2() < py1 && newPath.getY2() > py2)
							|| (newPath.getY2() > py1 && newPath.getY2() < py2)) {
						double dist = line1.ptLineDist(newPath.getX2(), newPath.getY2());
						closestDist = dist < closestDist ? dist : closestDist;
					}
				}

			}

			// if (angle < 20 && ((newPath.getX2() > px1 && newPath.getX2() <
			// px2)
			// || (newPath.getX2() < px1 && newPath.getX2() > px2)
			// || (newPath.getY2() < py1 && newPath.getY2() > py2)
			// || (newPath.getY2() > py1 && newPath.getY2() < py2))) {
			//
			// double dist = line1.ptLineDist(newPath.getX2(), newPath.getY2());
			// closestDist = dist < closestDist ? dist : closestDist;
			// }
		}
		return closestDist;

	}


	public static double angleBetween2Lines(Line2D line1, Line2D line2) {
		double angle1 = Math.atan2(line1.getY1() - line1.getY2(), line1.getX1() - line1.getX2());
		double angle2 = Math.atan2(line2.getY1() - line2.getY2(), line2.getX1() - line2.getX2());
		return Math.abs(Math.toDegrees(Math.abs(angle1) - Math.abs(angle2)));
	}

	private Set<Shape> resolveShapes(String svg) {

		InputStream inputStream = new ByteArrayInputStream(svg.getBytes());
		Set<Shape> shapes = new HashSet<Shape>();
		try {
			Document doc = getDocument(inputStream);
			NodeList elems = doc.getDocumentElement().getElementsByTagName("path");
			for (int i = 0; i < elems.getLength(); i++) {
				Node n = elems.item(i);
				Reader arg0 = new StringReader(((Element) n).getAttributeNS(null, "d"));
				Shape s = AWTPathProducer.createShape(arg0, GeneralPath.WIND_EVEN_ODD);
				// double lScale = s.getBounds().getHeight() / dimensionY;
				Path2D path = new Path2D.Double();
				double[] coords = new double[6];
				for (PathIterator pi = s.getPathIterator(null); !pi.isDone(); pi.next()) {
					// The type will be SEG_LINETO, SEG_MOVETO, or SEG_CLOSE
					// Because the Area is composed of straight lines
					int type = pi.currentSegment(coords);

					if (type == PathIterator.SEG_MOVETO) {
						path.moveTo(coords[0] / scale, (619 - coords[1]) / scale);
					} else if (type == PathIterator.SEG_LINETO) {
						path.lineTo(coords[0] / scale, (619 - coords[1]) / scale);
					} else {
						path.closePath();
					}
				}

				shapes.add(path);

				// shapes.add(s);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return shapes;
	}

	private Set<Line2D> resolveLines(String svg) {
		InputStream inputStream = new ByteArrayInputStream(svg.getBytes());
		Set<Line2D> shapes = new HashSet<Line2D>();
		try {
			Document doc = getDocument(inputStream);
			NodeList elems = doc.getDocumentElement().getElementsByTagName("line");
			for (int i = 0; i < elems.getLength(); i++) {
				Node n = elems.item(i);
				Element el = (Element) n;
				String x1 = el.getAttribute("x1");
				String x2 = el.getAttribute("x2");
				String y1 = el.getAttribute("y1");
				String y2 = el.getAttribute("y2");
				Line2D line = new Line2D.Double(Double.parseDouble(x1) / scale, (619 - Double.parseDouble(y1)) / scale,
						Double.parseDouble(x2) / scale, (619 - Double.parseDouble(y2)) / scale);

				shapes.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return shapes;
	}

	private boolean insidePolygon(List<Point2D> points, Point2D e) {
		int j = points.size() - 1;
		boolean oddNodes = false;

		for (int i = 0; i < points.size(); i++) {
			if ((points.get(i).getY() < e.getY() && points.get(j).getY() >= e.getY()
					|| points.get(j).getY() < e.getY() && points.get(i).getY() >= e.getY())
					&& (points.get(i).getX() <= e.getX() || points.get(j).getX() <= e.getX())) {
				if (points.get(i).getX()
						+ (e.getY() - points.get(i).getY()) / (points.get(j).getY() - points.get(i).getY())
								* (points.get(j).getX() - points.get(i).getX()) < e.getX()) {
					oddNodes = !oddNodes;
				}
			}
			j = i;
		}

		return oddNodes;
	}

	@Transient
	@JsonIgnore
	private BufferedImage map;

	private BufferedImage getMap() {
		if (map == null) {
			// ClassLoader classLoader = getClass().getClassLoader();
			InputStream stream = this.getClass().getResourceAsStream("bib_5.png");
			try {
				BufferedImage image = ImageIO.read(stream);
				map = image;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}

	public boolean isValidPointByPixel(Double x, Double y) {

		int pixelX = (int) Math.round(x * scale);
		int pixelY = (int) Math.round((dimensionY - y) * scale);

		if (pixelX < 0 || pixelY < 0 || pixelX > getMap().getWidth() - 1 || pixelY > getMap().getHeight() - 1) {
			return true;
		}

		int p = getMap().getRGB(pixelX, pixelY);

		// get alpha
		int a = (p >> 24) & 0xff;

		// get red
		int r = (p >> 16) & 0xff;

		// get green
		int g = (p >> 8) & 0xff;

		// get blue
		int b = p & 0xff;

		// if (r < 255 || g < 255 || b < 255) {
		if (a > 0) {
				return false;
			}


		return true;

	}

	public boolean isValidPoint(Double x, Double y) {

		for (Shape wall : walls) {
			Path2D path = new Path2D.Double();

			double[] coords = new double[6];
			List<Point2D> points = new ArrayList<Point2D>();

			for (PathIterator pi = wall.getPathIterator(null); !pi.isDone(); pi.next()) {
				// The type will be SEG_LINETO, SEG_MOVETO, or SEG_CLOSE
				// Because the Area is composed of straight lines
				int type = pi.currentSegment(coords);

				if (type == PathIterator.SEG_MOVETO) {
					path.moveTo(coords[0], coords[1]);
					points.add(new Point2D.Double(coords[0], coords[1]));
				} else if (type == PathIterator.SEG_LINETO) {
					path.lineTo(coords[0], coords[1]);
					points.add(new Point2D.Double(coords[0], coords[1]));
				} else {
					path.closePath();
				}
			}

			if (insidePolygon(points, new Point2D.Double(x, y))) {
				return false;
			}


			// if (path.contains(x * scale, y * scale)) {
			// return false;
			// }
		}
		return true;
	}


}
