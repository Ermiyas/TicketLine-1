package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.EventTicketsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.event.TopTenDetailsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.EventSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/events")
@Api(value = "events")
public class EventEndpoint {

    private final EventService eventService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public EventEndpoint(EventService eventService) {
        this.eventService = eventService;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create event", authorizations = {@Authorization(value = "apiKey")})
    public EventDTO createEvent(@RequestBody EventDTO eventDTO) {
        LOGGER.info("Event Endpoint: createEvent");
        try {
            return eventService.createEvent(eventDTO);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/topten")
    @ApiOperation(value = "Get top 10 events", authorizations = {@Authorization(value = "apiKey")})
    public List<EventTicketsDTO> findTopTenEvents(@RequestBody TopTenDetailsDTO details) {
        LOGGER.info("Event Endpoint: findTopTenEvents");
        Set<String> monthsSet = new HashSet<>(details.getMonths());
        Set<EventType> categoriesSet = new HashSet<>();

        for (String s: details.getCategories()) {
            categoriesSet.add(EventType.valueOf(s));
        }

        try {
            return new ArrayList<>(eventService.findTopTenEvents(monthsSet, categoriesSet));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all events", authorizations = {@Authorization(value = "apiKey")})
    public Page<EventDTO> findEventsFilteredByAttributes(@RequestParam(value = "eventName", required = false) String eventName,
                                                         @RequestParam(value = "eventType", required = false) String eventType,
                                                         @RequestParam(value = "content", required = false) String content,
                                                         @RequestParam(value = "description", required = false) String description,
                                                         @RequestParam(value = "duration", required = false) Integer duration,
                                                         @RequestParam(value = "artistName", required = false) String artistName,
                                                         @RequestParam(value = "page", required = false) Integer page) {
        if (page!= null && eventName == null && eventType == null && content == null && description == null && duration == null && artistName == null) {
            LOGGER.info("Event Endpoint: findAll");
            try {
                return eventService.findAll(page);
            } catch (ServiceException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        } else {
            LOGGER.info("Event Endpoint: findEventsFilteredByAttributes");
            try{
                EventType eventTypeConv = null;
                if (eventType != null) {
                    for (EventType type : EventType.values()
                    ) {
                        if (eventType.equalsIgnoreCase(type.name())) {
                            eventTypeConv = type;
                        }

                    }
                }
                EventSearchParametersDTO parameters = EventSearchParametersDTO.builder()
                    .setName(eventName)
                    .setContent(content)
                    .setDurationInMinutes(duration)
                    .setArtistName(artistName)
                    .setDescription(description)
                    .setEventType(eventTypeConv)
                    .build();
                LOGGER.info("Event Endpoint: findEventsFilteredByAttributes" + parameters.toString());
                Page<EventDTO> page1 = eventService.findAllFiltered(parameters, page);
                LOGGER.debug("Event Endpoint: " + page1.getContent().toString());
                return page1;
            }catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for events with those parameters: " + e.getMessage(), e);
            }catch (ServiceException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for events with those parameters", e);
            } catch (NotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No events are found for the given parameters:" + e.getMessage(), e);
            }
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/artist/{id}")
    public Page<EventDTO> findEventsFilteredByArtistID(@PathVariable Long id, @RequestParam(value = "page", required = false) Integer page) {
        LOGGER.info("Event Endpoint: findEventsFilteredByArtistID");
        try{
            return eventService.findEventsFilteredByArtistID(id, page);
        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for events by that artist: " + e.getMessage(), e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for events by that artist", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No events are found by that artist:" + e.getMessage(), e);
        }

    }
}
