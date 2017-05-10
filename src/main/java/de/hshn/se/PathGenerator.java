package de.hshn.se;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.DoubleStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hshn.se.domain.Measurement;
import de.hshn.se.domain.MeasurementDataset;
import de.hshn.se.domain.Store;
import de.hshn.se.domain.StoreMap;
import de.hshn.se.domain.Waypoint;

public class PathGenerator {

	private static final double l_N = 0.5;
	private static final double d_N = 0.1;
	private static final double R_ortho = 1d;
	private static final Logger log = LoggerFactory.getLogger(PathGenerator.class);
	private static final boolean TRACE_PARTICLE = false;

	public static Set<Waypoint> bpf(int numParciles, MeasurementDataset measurementSet, Store store, StoreMap map) {
		log.info("Start path creation");
		Random r = new Random();

		List<Measurement> measurements = extractMeasurements(measurementSet.getMeasurements());

		int numSteps = measurements.size();
		Waypoint[] waypoints = new Waypoint[numSteps];
		waypoints[0] = calculateStartPosition(measurementSet.getStartLat(), measurementSet.getStartLon(), map);

		// hack! startpunkt hat keinen zeitstempel
		waypoints[0].setTimestamp(measurements.get(0).getTimestamp() - 500);

		Particle[][] particles = new Particle[numParciles][numSteps];
		Particle[] initP = createInitialParticles(numParciles, waypoints[0], measurementSet.getStartAccuracy(), map);
		traceParticle(initP);

		for (int i = 0; i < numParciles; i++) {
			particles[i][0] = initP[i];
		}
		double[] weights = new double[numParciles];

		for (int k = 1; k < numSteps; k++) {
			log.info("processing step " + k + " of " + measurements.size());
			Measurement m = measurements.get(k - 1);

			Waypoint waypoint = new Waypoint();
			waypoint.setTimestamp(m.getTimestamp());
			waypoints[k] = waypoint;

			for (int i = 0; i < numParciles; i++) {

				// calculate noise for direction and distance
				double dNoise = r.nextGaussian() * Math.sqrt(d_N);
				double lNoise = r.nextGaussian() * Math.sqrt(l_N);
				double deltaOri = particles[i][k - 1].ori;

				double direction = m.getDirection() + dNoise + deltaOri;
				double distance = m.getLength() + lNoise;

				// dead reckoning
				double new_x = particles[i][k - 1].x + distance * Math.sin(direction);
				double new_y = particles[i][k - 1].y + distance * Math.cos(direction);

				particles[i][k] = new Particle(new_x, new_y, deltaOri);

				// map matching
				boolean validPath = map.isValidPath(particles[i][k - 1].x, particles[i][k - 1].y, particles[i][k].x,
						particles[i][k].y);

				double distanceToPath = map.distanceToPath(particles[i][k - 1].x, particles[i][k - 1].y,
						particles[i][k].x, particles[i][k].y);

				if (!validPath) {
					weights[i] = 0d;
				} else {
					weights[i] = Math.exp(-0.5 * distanceToPath * (1.0d / R_ortho) * distanceToPath);
				}

			}

			// normalize
			double[] weights_temp = new double[numParciles];
			double sum = DoubleStream.of(weights).sum();
			for (int i = 0; i < numParciles; i++) {
				weights_temp[i] = weights[i] / sum;
			}
			weights = weights_temp;

			// resample
			Particle[][] particles_temp = new Particle[numParciles][numSteps];
			double[] cumsum = new double[numParciles];
			for (int i = 0; i < numParciles; i++) {
				if (i == 0) {
					cumsum[i] = weights[i];
				} else {
					cumsum[i] = cumsum[i - 1] + weights[i];
				}
			}

			for (int i = 0; i < numParciles; i++) {
				double rand = Math.random();

				for (int j = 0; j < numParciles; j++) {
					if (j == 0) {
						if (rand < cumsum[j]) {
							particles_temp[i] = particles[j];
							break;
						}
					} else {
						if (rand < cumsum[j] && rand > cumsum[j - 1]) {
							particles_temp[i] = particles[j];
							// System.out.println("Partikel " + particles[j][k]
							// + " mit Gewicht " + weights[j]);
							break;
						}
					}
				}
			}

			for (int n = 0; n < numParciles; n++) {
				for (int o = 0; o < numSteps; o++) {
					particles[n][o] = particles_temp[n][o];
				}
			}

			if (TRACE_PARTICLE) {
				Particle[] particles_trace = new Particle[numParciles];
				for (int l = 0; l < numParciles; l++) {
					particles_trace[l] = particles[l][k];

				}

				traceParticle(particles_trace);
			}

		}

		// estimate state and do backtracking
		for (int k = 0; k < numSteps; k++) {
			double x = 0;
			double y = 0;
			for (int i = 0; i < numParciles; i++) {
				double antX = particles[i][k].x * weights[i];
				double antY = particles[i][k].y * weights[i];
				x = x + antX;
				y = y + antY;
			}
			waypoints[k].x(x).y(y);
		}

		log.info("Done path creation");
		return new HashSet<Waypoint>(Arrays.asList(waypoints));
	}

	private static Particle[] createInitialParticles(int numParciles, Waypoint start, Float accuracy, StoreMap map) {
		Random r = new Random();
		Particle[] particles = new Particle[numParciles];
		for (int i = 0; i < numParciles / 2; i++) {
			double ori = r.nextGaussian() * (Math.PI / 4);
			particles[i] = createParticle(start, accuracy, map, ori);
		}

		for (int i = numParciles / 2; i < numParciles; i++) {
			double ori = (r.nextGaussian() * (Math.PI / 4)) + Math.PI;
			particles[i] = createParticle(start, accuracy, map, ori);
		}
		return particles;
	}

	private static Particle createParticle(Waypoint start, Float accuracy, StoreMap map, double ori) {
		Random r = new Random();
		Double x;
		Double y;

		do {
			x = start.getX() + accuracy * r.nextGaussian();
			y = start.getY() + accuracy * r.nextGaussian();
		} while (!map.isValidPointByPixel(x, y));

		return new Particle(x, y, ori);

	}

	private static List<Measurement> extractMeasurements(String measurementsAsString) {
		List<Measurement> measurements = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			measurements = mapper.readValue(measurementsAsString, new TypeReference<List<Measurement>>() {
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (measurements == null || measurements.isEmpty()) {
			return null;
		}
		return measurements;
	}

	private static Waypoint calculateStartPosition(Double startLat, Double startLon, StoreMap map) {
		Waypoint start = new Waypoint();
		// double[] point = map.geodetic2Ned(startLat, startLon);
		// start.setX(point[0]);
		// start.setY(point[1]);

		start.setX(20d);
		start.setY(7d);
		return start;
	}

	private static void traceParticle(Particle[] particles) {
		if (TRACE_PARTICLE) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (Particle p : particles) {
				sb.append(p.x).append(" ");
			}
			sb.append("],[");
			for (Particle p : particles) {
				sb.append(p.y).append(" ");
			}
			sb.append("]");
			System.out.println(sb.toString());

		}
	}

}
