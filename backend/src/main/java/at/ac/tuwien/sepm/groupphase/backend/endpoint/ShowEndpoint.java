package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//TODO Class is unfinished

@RestController
@RequestMapping(value = "/shows")
@Api(value = "shows")
public class ShowEndpoint {

    private final ShowService showService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    ShowEndpoint(ShowService showService){
        this.showService = showService;
    }

    @RequestMapping(value = "/event", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all shows filtered by eventName", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findAllShowsFilteredByEventName(@RequestParam(value = "eventName") String eventName){
        LOGGER.info("Show Endpoint: Get all shows which belong to event \"" + eventName + " \"");
        try{
            // TODO
            //  return showService.findAllShowsFilteredByEventName(eventName);
            return showService.findAll(); // replace this line
        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows belonging to event with name " + eventName, e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows belonging to event with name " + eventName, e);
        }catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows belonging to event with name " + eventName + "were found: " + e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/location/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all shows filtered by location id", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findAllShowsFilteredByLocationID(@PathVariable("id") Integer locationID){
        LOGGER.info("Show Endpoint:  Get all shows filtered by location with id " + locationID);
        try{
            // TODO
            //  return showMapper.showToShowDTO(showService.findAllShowsFilteredByLocationID(locationID));
            return showService.findAll(); // replace this line
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows for that location", e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows for that location", e);
        }catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows for that location were found:" + e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    @ApiOperation(value = "Get all shows filtered by specified attributes", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findShowsFilteredByShowAttributes(
        @RequestParam(value = "eventName", required = false) String eventName,
        @RequestParam(value = "hallName", required = false) String hallName,
        @RequestParam(value="minPrice", required = false) Integer minPrice,
        @RequestParam(value="maxPrice", required = false) Integer maxPrice,
        @RequestParam(value="dateFrom", required = false) String dateFrom,
        @RequestParam(value="dateTo", required = false) String dateTo,
        @RequestParam(value="timeFrom", required = false) String timeFrom,
        @RequestParam(value="timeTo", required = false) String timeTo,
        @RequestParam(value="duration", required = false) Integer duration,

        @RequestParam(value = "country", required = false) String country,
        @RequestParam(value = "city", required = false) String city,
        @RequestParam(value = "postalCode", required = false) String postalCode,
        @RequestParam(value = "street", required = false) String street)
    {
        try {
            LOGGER.debug("\neventName: " + eventName + "\nhallName: " + hallName + "\nminPrice: " + minPrice + "\nmaxPrice: " + maxPrice +
                "\ndateFrom: " + dateFrom + "\ndateTo: " + dateTo + "\ntimeFrom: " + timeFrom + "\ntimeTo: " + timeTo +
                "\nduration: " +duration + "\ncountry: " + country + "\ncity: " + city + "\nstreet: " + street + "\npostalCode: " + postalCode);

                ShowSearchParametersDTO parameters = new ShowSearchParametersDTO.builder()
                    .setPriceInEuroFrom(minPrice)
                    .setPriceInEuroTo(maxPrice)
                    .setEventName(eventName)
                    .setHallName(hallName)
                    .setDateFrom(dateFrom == null ? null : LocalDate.parse(dateFrom, dateFormatter))
                    .setDateTo(dateTo == null ? null : LocalDate.parse(dateTo, dateFormatter))
                    .setTimeFrom(timeFrom == null ? null : LocalTime.parse(timeFrom, timeFormatter))
                    .setTimeTo(timeTo == null ? null : LocalTime.parse(timeTo, timeFormatter))
                    .setDurationInMinutes(duration)
                    .country(country)
                    .city(city)
                    .street(street)
                    .postalcode(postalCode)
                    .build();

                LOGGER.info("Get all shows filtered by specified attributes: " + parameters.toString());
                return showService.findAllShowsFiltered(parameters);

        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows with those parameters: " + e.getMessage(), e);
        }catch (at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows with those parameters", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows are found for the given parameters:" + e.getMessage(), e);
        }
    }
}
