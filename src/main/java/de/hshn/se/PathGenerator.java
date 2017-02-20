package de.hshn.se;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.DoubleStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hshn.se.domain.Measurement;
import de.hshn.se.domain.MeasurementDataset;
import de.hshn.se.domain.Store;
import de.hshn.se.domain.StoreMap;
import de.hshn.se.domain.Waypoint;

public class PathGenerator {

	private static final double l_N = 0.1;
	private static final double d_N = 0.1;
	private static final double R_ortho = 20;

	public static Set<Waypoint> bpf(int numParciles, MeasurementDataset measurementSet, Store store, StoreMap map) {
		Random r = new Random();

		List<Measurement> measurements = extractMeasurements(measurementSet.getMeasurements());

		int numSteps = measurements.size();
		Waypoint[] waypoints = new Waypoint[numSteps];
		waypoints[0] = calculateStartPosition(measurementSet.getStartLat(), measurementSet.getStartLon());
		Particle[][] particles = new Particle[numSteps][numParciles];
		particles[0] = createInitialParticles(numParciles, waypoints[0]);
		double[] weights = new double[numParciles];

		for (int k = 1; k < numSteps; k++) {
			Measurement m = measurements.get(k - 1);
			Waypoint waypoint = new Waypoint();
			waypoint.setTimestamp(m.getTimestamp());
			waypoints[k] = waypoint;

			for (int i = 0; i < numParciles; i++) {
				
				// calculate noise for direction and distance
				double dNoise = r.nextGaussian() * Math.sqrt(d_N);
				double lNoise = r.nextGaussian() * Math.sqrt(l_N);
				double deltaOri = particles[k - 1][i].ori;

				double direction = m.getDirection() + dNoise + deltaOri;
				double distance = m.getLength() + lNoise;

				// dead reckoning
				double new_x = particles[k - 1][i].x + distance * Math.cos(direction);
				double new_y = particles[k - 1][i].y + distance * Math.sin(direction);

				particles[k][i] = new Particle(new_x, new_y, deltaOri);

				boolean validPath = map.isValidPath(particles[k - 1][i].x, particles[k - 1][i].y,
						particles[k][i].x, particles[k][i].y);

				double distanceToPath = map.distanceToPath(particles[k - 1][i].x, particles[k - 1][i].y,
						particles[k][i].x, particles[k][i].y);

				// calculate weights
				if (!validPath) {
					weights[i] = 0;
				} else if (distanceToPath < 3) {
					Math.exp(-0.5 * distanceToPath * R_ortho * distanceToPath);
				} else {
					weights[i] = 1 / numParciles;
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
			Particle[] particles_temp = new Particle[numParciles];
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
					if ((j == 0 && rand > cumsum[j]) || (rand < cumsum[j] && rand > cumsum[j - 1])) {
						particles_temp[i] = particles[k][j];
						break;
					}
				}
			}

			particles[k] = particles_temp;

		}

		// estimate state and do backtracking
		for (int k = 1; k < numSteps; k++) {
			
		}


		return new HashSet<Waypoint>(Arrays.asList(waypoints));
	}


	private static Particle[] createInitialParticles(int numParciles, Waypoint waypoint) {
		// TODO Auto-generated method stub
		return null;
	}

	private static List<Measurement> extractMeasurements(String measurementsAsString) {
		List<Measurement> measurements = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			measurements = mapper.readValue(measurementsAsString,
					new TypeReference<List<Measurement>>() {
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

	private static Waypoint calculateStartPosition(Double startLat, Double startLon) {
		// TODO Auto-generated method stub
		return null;
	}

}
