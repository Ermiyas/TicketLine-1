package at.ac.tuwien.sepm.groupphase.backend.datagenerator.demo;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Profile("generateData")
@Component
public class SeatDataGenerator implements DataGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final SeatRepository seatRepository;
    private final SectorRepository sectorRepository;
    private final HallRepository hallRepository;

    public SeatDataGenerator(SeatRepository seatRepository, HallRepository hallRepository, SectorRepository sectorRepository) {
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
        this.sectorRepository = sectorRepository;
    }

    /*
    @Override
    public void generate(){
        if(seatRepository.count() > 0){
            LOGGER.info("Seats already generated");
        }else {
            LOGGER.info("Generating seats");
            Seat seat1 = Seat.builder().id(1L).seatNumber(22).seatRow(10).priceCategory(PriceCategory.EXPENSIVE).hall(hallRepository.getOne(1L)).build();
            Seat seat2 = Seat.builder().id(2L).seatNumber(25).seatRow(12).priceCategory(PriceCategory.CHEAP).hall(hallRepository.getOne(2L)).build();
            Seat seat3 = Seat.builder().id(3L).seatNumber(26).seatRow(1).priceCategory(PriceCategory.EXPENSIVE).hall(hallRepository.getOne(3L)).build();
            Seat seat4 = Seat.builder().id(4L).seatNumber(31).seatRow(22).priceCategory(PriceCategory.CHEAP).hall(hallRepository.getOne(1L)).build();
            Seat seat5 = Seat.builder().id(5L).seatNumber(2).seatRow(17).priceCategory(PriceCategory.AVERAGE).hall(hallRepository.getOne(7L)).build();
            Seat seat6 = Seat.builder().id(6L).seatNumber(14).seatRow(18).priceCategory(PriceCategory.AVERAGE).hall(hallRepository.getOne(6L)).build();
            Seat seat7 = Seat.builder().id(7L).seatNumber(5).seatRow(9).priceCategory(PriceCategory.EXPENSIVE).hall(hallRepository.getOne(7L)).build();
            Seat seat8 = Seat.builder().id(8L).seatNumber(5).seatRow(2).priceCategory(PriceCategory.EXPENSIVE).hall(hallRepository.getOne(8L)).build();
            Seat seat9 = Seat.builder().id(9L).seatNumber(25).seatRow(10).priceCategory(PriceCategory.AVERAGE).hall(hallRepository.getOne(9L)).build();
            Seat seat10 = Seat.builder().id(10L).seatNumber(26).seatRow(10).priceCategory(PriceCategory.AVERAGE).hall(hallRepository.getOne(9L)).build();

            seatRepository.saveAll(Arrays.asList(seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8, seat9, seat10));
        }
    }
    */
    @Override
    public void generate() {
        if (seatRepository.count() > 0) {
            LOGGER.info("Seats already generated");
        } else {
            LOGGER.info("Generating seats");
            List<Seat> seats = new ArrayList<>();
            for (Long id = 1L; id <= hallRepository.count()*5; id++) {
                Boolean seatsOrSectors = id % 2 == 0;
                seats = seatsOrSectors ? new ArrayList<>() : null;
                sectors = seatsOrSectors ? null : new ArrayList<>();
                if (seatsOrSectors) {
                    for (Long seatId = 1L; seatId <= NUM_OF_SEATS_PER_HALL; seatId++)
                        seats.add(Seat.builder()
                            .priceCategory(seatId % 3 == 0 ? PriceCategory.CHEAP : seatId % 3 == 1 ? PriceCategory.AVERAGE : PriceCategory.EXPENSIVE)
                            .seatNumber(customModInt(seatId, NUM_OF_SEAT_ROWS_PER_HALL))
                            .seatRow(Math.toIntExact((seatId - 1) / NUM_OF_SEAT_ROWS_PER_HALL + 1))
                            .hall(hallRepository.getOne(customMod(id, NUM_OF_HALLS)))
                            .build());
                } else {
                    for (Long sectorId = 1L; sectorId <= NUM_OF_SECTORS_PER_HALL; sectorId++)
                        sectors.add(Sector.builder()
                            .priceCategory(sectorId % 3 == 0 ? PriceCategory.CHEAP : sectorId % 3 == 1 ? PriceCategory.AVERAGE : PriceCategory.EXPENSIVE)
                            .sectorNumber(Math.toIntExact(customMod(sectorId, NUM_OF_SECTORS_PER_HALL)))
                            .hall(hallRepository.getOne(customMod(id, NUM_OF_HALLS)))
                            .build());
                }
                halls.add(Hall.builder()
                    .id(id)
                    .location(locationRepository.getOne(customMod(id, NUM_OF_LOCATIONS)))
                    .name(faker.bothify("HALL ????????"))
                    .seats(seatsOrSectors ? seats : null)
                    .sectors(seatsOrSectors ? null : sectors)
                    .build());
            }
            hallRepository.saveAll(halls);
        }
    }
}
