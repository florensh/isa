package de.hshn.se.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import de.hshn.se.domain.Store;
import de.hshn.se.domain.StoreMap;
import de.hshn.se.service.StoreMapService;
import de.hshn.se.service.StoreService;
import de.hshn.se.web.rest.util.HeaderUtil;
import de.hshn.se.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Store.
 */
@RestController
@RequestMapping("/api")
public class StoreResource {

	public static final ZonedDateTime DEFAULT_VALIDITY_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
			ZoneOffset.UTC);

	public static final ZonedDateTime DEFAULT_VALIDITY_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L),
			ZoneOffset.UTC);

	public static final String DEFAULT_URL = "AAAAAAAAAA";

	public static final String DEFAULT_WALL_MAP = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?> <svg xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:cc=\"http://creativecommons.org/ns#\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 421.20001 619.20001\" height=\"619.20001\" width=\"421.20001\" version=\"1.1\" id=\"svg3670\"> <path id=\"path5014\" d=\"M 3.3577297,567.41774 L 2.4912335,566.55125 L 1.6247372,565.68474 L 76.094499,3.1000001 L 414.98648,48.626474 L 356.49953,491.80001 L 351.99953,491.80001 L 355.14075,462.24077 L 351.80718,463.25126 L 345.22591,465.59967 L 336.56639,466.91094 L 331.53095,467.2607 L 322.49204,466.09017 L 313.45313,464.91963 L 307.68605,463.13836 L 296.40226,459.03514 L 283.86777,452.46175 L 276.84999,448.21033 L 270.4963,441.87003 L 264.14263,435.52974 L 261.58272,430.29748 L 259.02281,425.06521 L 258.61122,420.78165 L 258.19962,416.4981 L 259.67291,413.64905 L 261.14621,410.80001 L 262.2481,410.79401 L 268.48384,413.21861 L 273.61769,415.64931 L 273.13027,419.2997 L 272.64286,422.95009 L 275.19643,426.71693 L 277.74999,430.48377 L 283.15592,434.81693 L 288.56185,439.15009 L 294.85592,442.43452 L 301.14999,445.71894 L 308.34999,448.10273 L 318.24999,451.41504 L 320.94999,452.34355 L 331.01367,452.84465 L 341.07737,453.34575 L 344.77042,451.42082 L 348.46347,449.4959 L 352.13882,451.37091 L 355.81416,453.24593 L 356.51875,452.49801 L 357.22335,451.75009 L 364.34322,393.14332 L 269.19999,379.96015 L 312.23334,53.48769 L 94.600002,24.055632 L 64.448393,247.45 L 48.827745,245.38037 L 46.115537,267.17953 L 82.209348,272.43578 L 80.007422,287.5 L 43.551827,283 L 27.109172,408.46918 L 63.593562,414.01935 L 54.368899,480.69196 L 49.847479,480.12783 L 48.028066,492.55066 L 20.145027,488.35052 L 21.820309,476.94195 L 18.300324,476.50001 L 10.77005,532.47005 L 57.183932,539.77683 L 54.830257,554.99842 L 216.6555,576.9605 L 226.553,508.04368 L 251.3716,511.84018 L 251.2502,513.10101 L 236.34986,510.88999 L 225.94345,591.69999 L 336.49963,605.19999 L 347.39995,526.12721 L 277.32601,516.35528 L 277.84636,514.44372 L 351.99995,523.54756 L 340.26566,610.59999 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path5012\" d=\"M 173.8,514.17482 L 181.00791,460.13218 L 191.13211,462.01875 L 184.56571,515.20001 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path5008\" d=\"M 116.63439,494.33439 L 122.37885,452.72975 L 147.47349,457.15001 L 141.4565,498.10001 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path5006\" d=\"M 84.700002,497.29863 L 91.030278,447.4739 L 101.2213,449.5213 L 100.12009,457.79231 L 99.01888,466.06332 L 96.85,468.66018 L 95.049876,482.70509 L 93.072973,497.98745 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path5004\" d=\"M 234.29484,509.35915 L 234.84704,504.34228 L 236.23081,487.88237 L 231.3454,467.25364 L 242.59078,464.83078 L 248.04851,487.49831 L 246.83496,507.08467 L 246.08522,511.4777 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path5002\" d=\"M 169.75,426.09996 L 174.14173,389.01233 L 187.47282,390.51046 L 194.1789,399.7586 L 198.08481,411.30428 L 195.36571,430.59996 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path5000\" d=\"M 148.6,414.55576 L 143.65,414.94555 L 139.9745,413.8732 L 133.55255,411.38061 L 130.8061,409.96036 L 127.11372,405.76559 L 123.42132,401.57081 L 122.51568,398.31041 L 121.61382,391.00001 L 121.61782,386.95001 L 123.73042,382.41322 L 125.84301,377.87645 L 128.89662,375.29907 L 135.55023,371.08061 L 139.15023,369.43953 L 144.32523,369.41973 L 149.50023,369.39993 L 149.45123,372.32493 L 147.15038,390.21008 L 169.46807,393.69993 L 170.20029,393.69993 L 170.20029,396.03892 L 170.20029,398.3779 L 168.69822,401.54328 L 167.19615,404.70867 L 164.12844,407.68799 L 161.06073,410.66731 L 157.30551,412.4166 L 153.55029,414.1659 L 148.60029,414.55569 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4998\" d=\"M 49.486389,406.17267 L 51.021792,392.45638 L 69.231157,394.6844 L 67.407421,408.10001 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4996\" d=\"M 100.13695,392.60789 L 101.38178,381.60511 L 102.89254,380.44418 L 104.4033,379.28325 L 106.6533,379.65903 L 113.1783,380.59786 L 117.4533,381.16091 L 119.2258,383.3008 L 118.10873,394.5508 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4994\" d=\"M 32.240071,366.88462 L 35.691893,340.77679 L 73.173088,345.82468 L 70.218438,372.10001 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4992\" d=\"M 93.630089,337.49631 L 94.984276,326.90407 L 242.20001,347.00623 L 241.07501,357.70001 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4990\" d=\"M 96.15898,317.03911 L 97.7,306.44968 L 245.32794,326.83226 L 243.8787,337.00001 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4988\" d=\"M 37.578681,327.10001 L 41.166512,300.98311 L 78.788618,305.5886 L 75.199184,330.78227 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4986\" d=\"M 100.67358,294.45384 L 102.12976,284.17025 L 248.30402,303.20401 L 247.14484,314.05001 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4982\" d=\"M 103.37565,273.23565 L 104.62952,263.2 L 250.49668,283.62958 L 248.97772,294.33589 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4980\" d=\"M 105.88935,254.59392 L 107.51295,243.681 L 254.05872,263.27413 L 252.39399,273.325 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4978\" d=\"M 108.83989,231.94482 L 110.10741,221.8 L 256.81769,241.51769 L 255.57049,251.5 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4976\" d=\"M 111.48366,212.53214 L 112.66577,202.47424 L 258.85001,220.52091 L 257.5565,231.7 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4974\" d=\"M 114.85,193.41964 L 116.31784,182.12217 L 262.00001,201.18157 L 260.24456,212.8 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4972\" d=\"M 262.45001,191.70253 L 117.32198,171.92198 L 118.16018,161.25467 L 264.4332,180.7332 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4968\" d=\"M 119.63989,150.04482 L 120.90741,139.9 L 266.99525,159.32034 L 265.46954,169.42628 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4966\" d=\"M 122.80143,127.35342 L 124.36821,116.13178 L 198.1,125.58157 L 196.6711,136.74055 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4964\" d=\"M 237.70001,134.3805 L 246.70001,60.448226 L 257.56702,62.223255 L 248.41844,135.4 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4962\" d=\"M 199.55,113.74073 L 125.84817,103.90006 L 127.39641,92.743652 L 201.25954,102.2596 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4960\" d=\"M 128.92437,79.150001 L 130.15,69.273983 L 204.62436,79.464351 L 203.37049,89.500001 Z\" style=\"display:inline;fill:#000000\" /> <path id=\"path4612\" d=\"M 132.17565,55.435651 L 133.42952,45.400001 L 207.9021,55.205939 L 206.16571,66.100001 Z\" style=\"display:inline;fill:#000000\" /> </svg>";

	public static final String DEFAULT_PATH_MAP = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?> <svg xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:cc=\"http://creativecommons.org/ns#\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 421.20001 619.20001\" height=\"619.20001\" width=\"421.20001\" version=\"1.1\" id=\"svg3670\"> <line x1=\"348.30001\" y1=\"508.34747\" x2=\"263.68475\" y2=\"507.0356\" id=\"path4263\" /> <line x1=\"350.2678\" y1=\"506.37967\" x2=\"237.44746\" y2=\"447.34577\" id=\"path4269\" /> <line x1=\"265.65255\" y1=\"507.0356\" x2=\"259.08357\" y2=\"554.31099\" id=\"path4271\" /> <line x1=\"294.51357\" y1=\"61.001696\" x2=\"255.81356\" y2=\"370.92967\" id=\"path4273\" /> <line x1=\"115.44407\" y1=\"36.076271\" x2=\"293.85763\" y2=\"61.657628\" id=\"path4275\" /> <line x1=\"87.894917\" y1=\"237.44746\" x2=\"117.41187\" y2=\"36.076271\" id=\"path4277\" /> <line x1=\"258.43729\" y1=\"345.67628\" x2=\"85.92712\" y2=\"320.75085\" id=\"path4279\" /> <line x1=\"261.06102\" y1=\"322.71865\" x2=\"47.22712\" y2=\"292.54577\" id=\"path4281\" /> <line x1=\"92.486443\" y1=\"257.12543\" x2=\"266.96441\" y2=\"280.08306\" id=\"path4285\" /> <line x1=\"269.58814\" y1=\"259.42119\" x2=\"87.894917\" y2=\"235.1517\" id=\"path4289\" /> <line x1=\"91.174578\" y1=\"214.48984\" x2=\"272.21187\" y2=\"238.75933\" id=\"path4291\" /> <line x1=\"279.09916\" y1=\"176.1178\" x2=\"100.35763\" y2=\"152.50424\" id=\"path4293\" /> <line x1=\"228.68561\" y1=\"51.762374\" x2=\"218.18104\" y2=\"144.75124\" id=\"path4295\" /> <line x1=\"103.96525\" y1=\"130.85848\" x2=\"282.37882\" y2=\"154.8\" id=\"path4297\" /> <line x1=\"107.24492\" y1=\"107.90084\" x2=\"218.98812\" y2=\"122.90771\" id=\"path4299\" /> <line x1=\"110.52458\" y1=\"82.975425\" x2=\"222.13195\" y2=\"98.638217\" id=\"path4301\" /> <line x1=\"114.13221\" y1=\"59.689831\" x2=\"225.21949\" y2=\"76.280254\" id=\"path4303\" /> <line x1=\"255.15763\" y1=\"150.20848\" x2=\"265.65255\" y2=\"58.05\" id=\"path4307\" /> <line x1=\"97.405932\" y1=\"173.82203\" x2=\"276.47543\" y2=\"198.09153\" id=\"path4309\" /> <line x1=\"274.50763\" y1=\"219.40933\" x2=\"94.126274\" y2=\"195.13983\" id=\"path4311\" /> <line x1=\"88.222883\" y1=\"234.82373\" x2=\"93.470341\" y2=\"272.53984\" id=\"path4313\" /> <line x1=\"61.329663\" y1=\"513.9229\" x2=\"93.470341\" y2=\"272.21187\" id=\"path4315\" /> <line x1=\"51.490679\" y1=\"257.78136\" x2=\"91.174578\" y2=\"263.35678\" id=\"path4317\" /> <line x1=\"92.158477\" y1=\"206.29068\" x2=\"70.512714\" y2=\"261.71696\" id=\"path4319\" /> <line x1=\"255.81356\" y1=\"369.94577\" x2=\"82.319493\" y2=\"347.31611\" id=\"path4321\" /> <line x1=\"71.824576\" y1=\"435.86696\" x2=\"238.10339\" y2=\"447.67374\" id=\"path4323\" /> <line x1=\"164.21614\" y1=\"470.00674\" x2=\"79.695764\" y2=\"376.83306\" id=\"path4325\" /> <line x1=\"250.71219\" y1=\"409.48183\" x2=\"256.14153\" y2=\"367.97797\" id=\"path4327\" /> <line x1=\"202.35509\" y1=\"562.78984\" x2=\"210.22628\" y2=\"469.3195\" id=\"path4331\" /> <line x1=\"168.24661\" y1=\"443.08221\" x2=\"150.20847\" y2=\"556.55848\" id=\"path4333\" /> <line x1=\"112.49238\" y1=\"440.13052\" x2=\"97.733907\" y2=\"550.32713\" id=\"path4335\" /> <line x1=\"204.97882\" y1=\"515.23476\" x2=\"160.37543\" y2=\"557.87035\" id=\"path4337\" /> <line x1=\"22.301695\" y1=\"509.9873\" x2=\"188.58051\" y2=\"530.64916\" id=\"path4339\" /> <line x1=\"75.760171\" y1=\"547.7034\" x2=\"203.33898\" y2=\"562.78984\" id=\"path4341\" /> <line x1=\"61.657628\" y1=\"513.59493\" x2=\"77.400002\" y2=\"547.37543\" id=\"path4343\" /> <line x1=\"64.609323\" y1=\"488.99747\" x2=\"23.61356\" y2=\"509.9873\" id=\"path4345\" /> <line x1=\"327.90301\" y1=\"563.96702\" x2=\"258.19856\" y2=\"552.75991\" id=\"path4347\" /> <line x1=\"209.57034\" y1=\"470.95933\" x2=\"254.5017\" y2=\"370.6017\" id=\"path4357\" /> <line x1=\"177.75763\" y1=\"361.41865\" x2=\"305.00848\" y2=\"482.9301\" id=\"path4359\" /> <line x1=\"281.53517\" y1=\"162.80692\" x2=\"281.53517\" y2=\"161.87929\" id=\"path4361\" /> <line x1=\"83.631358\" y1=\"340.75679\" x2=\"43.619492\" y2=\"335.50933\" id=\"path4363\" /> <line x1=\"77.727968\" y1=\"384.70424\" x2=\"41.32373\" y2=\"379.12882\" id=\"path4365\" /> <line id=\"path4215\" x1=\"250.69153\" y1=\"407.70078\" x2=\"265.53358\" y2=\"506.957\" /> <line id=\"path4217\" x1=\"257.18493\" y1=\"352.50689\" x2=\"164.88592\" y2=\"472.17094\" /> <line id=\"path4231\" x1=\"109.54068\" y1=\"438.49068\" x2=\"82.647459\" y2=\"353.2195\" /> <line id=\"path4233\" x1=\"137.4178\" y1=\"354.85933\" x2=\"78.711866\" y2=\"384.70425\" /> </svg>";

	public static final Double DEFAULT_DIMENSION_X = 25.0805D;

	public static final Double DEFAULT_DIMENSION_Y = 36.9224D;

	public static final Double DEFAULT_SCALE = 16.7648907D;

    private final Logger log = LoggerFactory.getLogger(StoreResource.class);
        
    @Inject
    private StoreService storeService;

	@Inject
	private StoreMapService storeMapService;

    /**
     * POST  /stores : Create a new store.
     *
     * @param store the store to create
     * @return the ResponseEntity with status 201 (Created) and with body the new store, or with status 400 (Bad Request) if the store has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stores")
    @Timed
    public ResponseEntity<Store> createStore(@Valid @RequestBody Store store) throws URISyntaxException {
        log.debug("REST request to save Store : {}", store);
        if (store.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("store", "idexists", "A new store cannot already have an ID")).body(null);
        }
        Store result = storeService.save(store);

		StoreMap storeMap = new StoreMap().validityStart(ZonedDateTime.now()).validityEnd(ZonedDateTime.now())
				.url(DEFAULT_URL).wallMap(DEFAULT_WALL_MAP).pathMap(DEFAULT_PATH_MAP).dimensionX(DEFAULT_DIMENSION_X)
				.dimensionY(DEFAULT_DIMENSION_Y).scale(DEFAULT_SCALE);

		storeMap.setStore(store);
		this.storeMapService.save(storeMap);

        return ResponseEntity.created(new URI("/api/stores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("store", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stores : Updates an existing store.
     *
     * @param store the store to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated store,
     * or with status 400 (Bad Request) if the store is not valid,
     * or with status 500 (Internal Server Error) if the store couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stores")
    @Timed
    public ResponseEntity<Store> updateStore(@Valid @RequestBody Store store) throws URISyntaxException {
        log.debug("REST request to update Store : {}", store);
        if (store.getId() == null) {
            return createStore(store);
        }
        Store result = storeService.save(store);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("store", store.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stores : get all the stores.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stores in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/stores")
    @Timed
    public ResponseEntity<List<Store>> getAllStores(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Stores");
        Page<Store> page = storeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stores/:id : get the "id" store.
     *
     * @param id the id of the store to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the store, or with status 404 (Not Found)
     */
    @GetMapping("/stores/{id}")
    @Timed
    public ResponseEntity<Store> getStore(@PathVariable Long id) {
        log.debug("REST request to get Store : {}", id);
        Store store = storeService.findOne(id);
        return Optional.ofNullable(store)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stores/:id : delete the "id" store.
     *
     * @param id the id of the store to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stores/{id}")
    @Timed
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        log.debug("REST request to delete Store : {}", id);
        storeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("store", id.toString())).build();
    }

}
