package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.seat.SeatDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.sector.SectorDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

//TODO add missing attributes
public class HallDTO {

    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The name of the hall")
    private String name;

    @ApiModelProperty(name = "The location of the hall")
    private LocationDTO location;

    @ApiModelProperty(name = "List of seats in the hall")
    private List<SeatDTO> seats;

    @ApiModelProperty(name = "List of sectors in the hall")
    private List<SectorDTO> sectors;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public List<SectorDTO> getSectors() {
        return sectors;
    }

    public void setSectors(List<SectorDTO> sectors) {
        this.sectors = sectors;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }

    public static HallDTOBuilder builder() {
        return new HallDTOBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HallDTO hallDTO = (HallDTO) o;
        return id.equals(hallDTO.id) &&
            name.equals(hallDTO.name) &&
            location.equals(hallDTO.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }

    @Override
    public String toString() {
        return "HallDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", location=" + location +
            '}';
    }

    public static final class HallDTOBuilder{

        private Long id;
        private String name;
        private LocationDTO location;
        private List<SeatDTO> seats;
        private List<SectorDTO> sectors;

        private HallDTOBuilder(){}

        public HallDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public HallDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HallDTOBuilder location(LocationDTO location) {
            this.location = location;
            return this;
        }

        public HallDTOBuilder seats(List<SeatDTO> seats){
            this.seats = seats;
            return this;
        }

        public HallDTOBuilder sectors(List<SectorDTO> sectors){
            this.sectors = sectors;
            return this;
        }

        public HallDTO build() {
            HallDTO hall = new HallDTO();
            hall.setId(id);
            hall.setName(name);
            hall.setLocation(location);
            hall.setSeats(seats);
            hall.setSectors(sectors);
            return hall;
        }
    }

    //TODO: incorporate seat and sector lists into equals toString and hashcode methods
}
