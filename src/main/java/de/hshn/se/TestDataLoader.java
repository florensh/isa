package de.hshn.se;

import java.time.ZonedDateTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import de.hshn.se.config.Constants;
import de.hshn.se.domain.Store;
import de.hshn.se.domain.Visit;
import de.hshn.se.domain.Visitor;
import de.hshn.se.domain.Waypoint;
import de.hshn.se.repository.StoreRepository;
import de.hshn.se.repository.VisitRepository;
import de.hshn.se.repository.VisitorRepository;
import de.hshn.se.repository.WaypointRepository;

@Component
public class TestDataLoader {

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private VisitorRepository visitorRepository;

	@Autowired
	private VisitRepository visitRepository;

	@Autowired
	private WaypointRepository waypointRepository;

	@PostConstruct
	@Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
	public void loadData() {
		Store store = new Store();
		store.setName("Kaufland Neckarsulm");
		store.setCity("Neckarsulm");
		store.setZip("74206");
		store.setCountry("Deutschland");
		store.setStreet("Hauptstraße 1");

		store = this.storeRepository.save(store);

		Visitor visitor = new Visitor();
		visitor = this.visitorRepository.save(visitor);

		Visit visit = new Visit();
		visit.setDateOfVisit(ZonedDateTime.now());
		visit.store(store);
		visit.setVisitor(visitor);
		visit = this.visitRepository.save(visit);

		Waypoint wp1 = new Waypoint();
		wp1.setX(480.0f);
		wp1.setY(50.0f);
		wp1.setVisit(visit);
		wp1.setTimestamp(0l);
		this.waypointRepository.save(wp1);
		
		
		Waypoint wp2 = new Waypoint();
		wp2.setX(480.0f);
		wp2.setY(200.0f);
		wp2.setVisit(visit);
		wp2.setTimestamp(1000l);
		this.waypointRepository.save(wp2);

	}

}