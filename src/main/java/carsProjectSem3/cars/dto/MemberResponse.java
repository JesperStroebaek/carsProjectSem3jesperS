package carsProjectSem3.cars.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import carsProjectSem3.cars.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponse {
  String username; //Remember this is the primary key
  String email;
  String firstName;
  String lastName;
  String street;
  String city;
  String zip;
  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",shape = JsonFormat.Shape.STRING)
  LocalDateTime created;

  @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss",shape = JsonFormat.Shape.STRING)
  LocalDateTime edited;
  Integer ranking;

  List<ReservationResponse> reservations;


  //Convert Member Entity to Member DTO
  public MemberResponse(Member m, boolean includeAll) {
    this.username = m.getUsername();
    this.email = m.getEmail();
    this.street = m.getStreet();
    this.firstName =m.getFirstName();
    this.lastName = m.getLastName();
    this.city = m.getCity();
    this.zip = m.getZip();

    if(includeAll){
      this.created = m.getCreated();
      this.edited = m.getEdited();
      this.ranking = m.getRanking();
    }

    if(m.getReservations().size()>0){
      reservations = m.getReservations().stream().map(r->ReservationResponse.builder()
                      .id(r.getId())
                      .carId(r.getCar().getId())
                      .carBrand(r.getCar().getBrand())
                      .rentalDate(r.getRentalDate())
                      .build()
                       ).collect(Collectors.toList());
    }
  }
}
