package carsProjectSem3.cars.entity;


import carsProjectSem3.security.entity.UserWithRoles;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends UserWithRoles {

  // Id is username from UserWithRoles

  //Make sure you understand why we CANNOT make any of the fields private using the inheritance strategy we are
  @Column(length = 30) //I Wish could be made NOT NULL
  private String firstName;

  @Column(length = 50)
  private String lastName; //I Wish could be made NOT NULL

  @Column(length = 50)  //I Wish could be made NOT NULL
  private String street;

  @Column(length = 50) //I Wish could be made NOT NULL
  private String city;

  @Column(length = 50) //I Wish could be made NOT NULL
  private String zip;

  private boolean approved;
  private int ranking;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  List<Reservation> reservations = new ArrayList<>();

  public void addReservation(Reservation reservation) {
    reservations.add(reservation);
    //reservation.setMember(this);
  }

  public Member(String user, String password, String email, String firstName, String lastName, String street, String city, String zip) {
    super(user, password, email);
    this.firstName = firstName;
    this.lastName = lastName;
    this.street = street;
    this.city = city;
    this.zip = zip;
  }
}
