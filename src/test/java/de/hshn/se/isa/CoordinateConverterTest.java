package de.hshn.se.isa;

import org.junit.Test;

public class CoordinateConverterTest {

	@Test
	public void testGlobalToLocal() {
		// Point2D res = CoordinateConverter.globalToLocal(49.122926, 9.211318);
		// System.out.println("x:" + res.getX() + " y:" + res.getY());

		double lat = 49.122900d;
		double lon = 9.211127d;

		double lat0 = 49.122926;//
		double lat1 = 49.123283;

		double lon0 = 9.211009;
		double lon1 = 9.211307;//

		double delta_lon = lon1 - lon0;
		double delta_lat = lat1 - lat0;

		double delta_x = 25.0805;
		double delta_y = 36.9224;

		double vertical_scale = delta_y / delta_lat;
		double horizontal_scale = delta_x / delta_lon;

		double y = (lat - lat0) * vertical_scale;
		double x = (lon - lon0) * horizontal_scale;

		System.out.println("X: " + x + " / Y: " + y + " h_fac: " + horizontal_scale + " / v_fac: " + vertical_scale);

	}

}
