package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.location.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private LocationRepository locationRepository;
    private LocationMapper locationMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper){
        this.locationMapper = locationMapper;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<LocationDTO> findLocationsFiltered(String country, String city, String street, String postalCode, String description) throws ServiceException{
        LOGGER.info("Location Service: findLocationsFiltered()");
        try{
            if(country == "") country = null;
            if(city == "") city = null;
            if(street == "") street = null;
            if(postalCode == "") postalCode = null;
            if(description == "") description = null;

            return locationMapper.locationToLocationDTO(locationRepository.findLocationsFiltered(country, city, street, postalCode, description));
        }catch (PersistenceException e){
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
