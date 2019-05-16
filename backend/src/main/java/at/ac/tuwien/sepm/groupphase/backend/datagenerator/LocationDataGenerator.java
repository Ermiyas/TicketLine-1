package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Profile("generateData")
@Component
public class LocationDataGenerator implements DataGenerator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final LocationRepository locationRepository;

    public LocationDataGenerator(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    @Override
    public void generate(){
        if(locationRepository.count() > 0){
            LOGGER.info("Locations already generated");
        }else {
            LOGGER.info("Generating locations");
            Location location1 = Location.builder().id(1L).country("Austria").city("Vienna").postalcode("1090").street("Tendlergasse 12").build();
            Location location2 = Location.builder().id(2L).country("Austria").city("Vienna").postalcode("1220").street("Josef Baumann Gasse 8").build();
            Location location3 = Location.builder().id(3L).country("Austria").city("Vienna").postalcode("1010").street("Maria-Theresien-Platz").build();
            Location location4 = Location.builder().id(4L).country("Austria").city("Vienna").postalcode("1211").street("Siemensstraße 90").build();
            Location location5 = Location.builder().id(5L).country("Austria").city("Vienna").postalcode("1010").street("Burgring 7").build();
            Location location6 = Location.builder().id(6L).country("Austria").city("Vienna").postalcode("1030").street("Prinz Eugen-Straße 27").build();
            Location location7 = Location.builder().id(7L).country("Austria").city("Vienna").postalcode("1020").street("Oswald-Thomas-Platz 1").build();
            Location location8 = Location.builder().id(8L).country("Austria").city("Vienna").postalcode("1130").street("Schönbrunner Schloßstraße").build();
            Location location9 = Location.builder().id(9L).country("Austria").city("Vienna").postalcode("1060").street("Gumpendorfer Straße 142").build();
            Location location10 = Location.builder().id(10L).country("Austria").city("Vienna").postalcode("1050").street("Burggasse 121").build();

            locationRepository.saveAll(Arrays.asList(location1, location2, location3, location4, location5,
                location6, location7, location8, location9, location10));
        }
    }
}
