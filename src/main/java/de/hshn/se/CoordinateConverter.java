package de.hshn.se;


import java.awt.geom.Point2D;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

public class CoordinateConverter {

	public static Point2D globalToLocal(double lat, double lon) {
		CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
		CRSFactory csFactory = new CRSFactory();
		final String LOCAL_PARAM = "+proj=tmerc +ellps=WGS84 +datum=WGS84 +units=m +no_defs +lat_0=49.122954 +lon_0=49.122954";
		CoordinateReferenceSystem crs = csFactory.createFromParameters(null, LOCAL_PARAM);
		final String WGS84_PARAM = "+title=long/lat:WGS84 +proj=longlat +ellps=WGS84 +datum=WGS84 +units=degrees";
		CoordinateReferenceSystem WGS84 = csFactory.createFromParameters("WGS84", WGS84_PARAM);

		CoordinateTransform trans = ctFactory.createTransform(WGS84, crs);

		ProjCoordinate p = new ProjCoordinate();
		ProjCoordinate p2 = new ProjCoordinate();
		p.x = lon;
		p.y = lat;

		/*
		 * Transform point
		 */
		trans.transform(p, p2);
		Point2D res = new Point2D.Double(p2.x, p2.y);

		return res;
	}

}
