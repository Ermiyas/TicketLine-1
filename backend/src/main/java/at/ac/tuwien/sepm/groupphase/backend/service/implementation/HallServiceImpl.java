package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.hall.HallMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.HallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public HallServiceImpl(HallRepository hallRepository, HallMapper hallMapper) {
        this.hallMapper = hallMapper;
        this.hallRepository = hallRepository;
    }

    @Override
    public List<HallDTO> findAllHalls() {
        LOGGER.info("Finding all halls saved in the system");
        return hallMapper.hallListToHallDTOs(hallRepository.findAll());
    }

    @Override
    public HallDTO addHall(HallDTO hallDTO) {
        LOGGER.info("Validating and adding hall: " + hallDTO.toString());
        // map hall dto to entity
        Hall hall = hallMapper.hallDTOToHall(hallDTO);
        //sets child column in ManyToOne relationship and validate seats/sectors;
        if (!isEmpty(hall.getSeats())) {
            for(Seat seat : hall.getSeats()){
                seat.setHall(hall);
            }
        } else if (!isEmpty(hall.getSectors())) {
            for(Sector sector : hall.getSectors()){
                sector.setHall(hall);
            }
        }
        return hallMapper.hallToHallDTO(hallRepository.save(hall));
    }
}
